package com.webapp.common.allure;

import com.codeborne.selenide.WebDriverRunner;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import io.qameta.allure.model.*;
import io.qameta.allure.util.ResultsUtils;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.Tag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
public class AllureResultTemplateProcessor {

    public AllureResultTemplate generateAllureResultTemplate(JoinPoint joinPoint, Throwable throwable) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String methodName = methodSignature.getDeclaringTypeName() + "." + methodSignature.getMethod().getName();
        return AllureResultTemplate
                .getFinishedBrokenTestTemplate(methodName, throwable)
                .processTestClassAnnotations(methodSignature)
                .addScreenshotIfPossible(throwable)
                .addBrowserLogsIfPossible();
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @EqualsAndHashCode
    public static class AllureResultTemplate {

        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("statusDetails")
        @Expose
        public StatusDetails statusDetails;
        @SerializedName("stage")
        @Expose
        public String stage;
        @SerializedName("attachments")
        @Expose
        public List<Attachment> attachments;
        @SerializedName("uuid")
        @Expose
        public String uuid;
        @SerializedName("historyId")
        @Expose
        public String historyId;
        @SerializedName("fullName")
        @Expose
        public String fullName;
        @SerializedName("labels")
        @Expose
        public List<Label> labels;
        @SerializedName("links")
        @Expose
        public List<Link> links;

        static AllureResultTemplate getFinishedBrokenTestTemplate(String methodName, Throwable throwable) {
            return AllureResultTemplate.builder()
                    .name(methodName)
                    .status(Status.BROKEN.value())
                    .statusDetails(new StatusDetails()
                            .setKnown(false)
                            .setMuted(false)
                            .setFlaky(false)
                            .setMessage(throwable.getMessage())
                            .setTrace(AllureAttachmentHelper.getStackTrace(throwable)))
                    .stage(Stage.FINISHED.value())
                    .uuid(UUID.randomUUID().toString())
                    .historyId(UUID.randomUUID().toString())
                    .attachments(new ArrayList<>())
                    .labels(new ArrayList<>())
                    .links(new ArrayList<>())
                    .build();
        }

        public String toString() {
            try {
                return new ObjectMapper().writeValueAsString(this);
            } catch (JsonProcessingException e) {
                throw new IllegalStateException("Failed to get JSON from AllureResultTemplate", e);
            }
        }

        private void addAttachment(Attachment attachment) {
            this.attachments.add(attachment);
        }

        private void addLabel(Label label) {
            this.labels.add(label);
        }

        private void addLink(Link link) {
            this.links.add(link);
        }

        private AllureResultTemplate addScreenshotIfPossible(Throwable throwable) {
            if (WebDriverRunner.hasWebDriverStarted()) {
                addAttachment(AllureAttachmentHelper.getScreenshotAttachment(throwable));
            }
            return this;
        }

        private AllureResultTemplate addBrowserLogsIfPossible() {
            if (WebDriverRunner.hasWebDriverStarted()) {
                addAttachment(AllureAttachmentHelper.getBrowserLogs());
            }
            return this;
        }

        private AllureResultTemplate processTestClassAnnotations(MethodSignature signature) {
            List annotations = Collections.emptyList();

            // process annotations set on class
            try {
                annotations = List.of(Class.forName(signature.getDeclaringTypeName()).getAnnotations());
            } catch (ClassNotFoundException ex) {
                log.warn("Error occurred while getting class annotations", ex);
            }

            annotations.forEach(annotation -> {
                if (annotation instanceof Story) {
                    this.addLabel(ResultsUtils.createStoryLabel(((Story) annotation).value()));
                }
                if (annotation instanceof Feature) {
                    this.addLabel(ResultsUtils.createFeatureLabel(((Feature) annotation).value()));
                }
                if (annotation instanceof Epic) {
                    this.addLabel(ResultsUtils.createEpicLabel(((Epic) annotation).value()));
                }
                if (annotation instanceof Owner) {
                    this.addLabel(ResultsUtils.createOwnerLabel(((Owner) annotation).value()));
                }
                if (annotation instanceof Tag) {
                    this.addLabel(ResultsUtils.createTagLabel(((Tag) annotation).value()));
                }
                if (annotation instanceof io.qameta.allure.Link) {
                    this.addLink(new Link()
                            .setName(((io.qameta.allure.Link) annotation).name())
                            .setUrl(((io.qameta.allure.Link) annotation).url()));
                }
            });

            // set default labels
            String declaringTypeName = signature.getDeclaringTypeName();
            this.addLabel(ResultsUtils.createSuiteLabel(declaringTypeName));
            this.addLabel(ResultsUtils.createTestClassLabel(declaringTypeName));
            this.addLabel(ResultsUtils.createPackageLabel(declaringTypeName));
            this.addLabel(ResultsUtils.createTestMethodLabel(signature.getName()));

            return this;
        }
    }
}
