package edu.stanford.bmir.protege.web.client.app;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.library.text.PlaceholderTextBox;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Mar 2017
 */
public class ApplicationSettingsViewImpl extends Composite implements ApplicationSettingsView {

    interface ApplicationSettingsViewImplUiBinder extends UiBinder<HTMLPanel, ApplicationSettingsViewImpl> {

    }

    private static ApplicationSettingsViewImplUiBinder ourUiBinder = GWT.create(ApplicationSettingsViewImplUiBinder.class);

    @UiField
    TextBox applicationNameField;

    @UiField
    TextBox systemNotificationEmailAddressField;

    @UiField
    CheckBox accountCreationEnabledCheckBox;

    @UiField
    CheckBox projectCreationEnabledCheckBox;

    @UiField
    CheckBox projectUploadEnabledCheckBox;

    @UiField
    CheckBox emailNotificationsEnabledCheckBox;

    @UiField
    ListBox applicationSchemeField;

    @UiField
    PlaceholderTextBox applicationHostField;

    @UiField
    PlaceholderTextBox applicationPathField;

    @UiField
    PlaceholderTextBox applicationPortField;

    @UiField
    Button rebuildPermissionsButton;

    @UiField
    Button applyButton;

    @UiField
    TabLayoutPanel tabPanel;

    @UiField
    PlaceholderTextBox maxUploadSize;

    private Runnable applySettingsHandler = () -> {};

    private Runnable rebuildPermissionsHandler = () -> {};

    @Inject
    public ApplicationSettingsViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        applicationHostField.setValidation(ApplicationSettingsPresenter.HOST_REGEXP.getSource());
        applicationPathField.setValidation(ApplicationSettingsPresenter.PATH_REGEXP.getSource());
        applicationPortField.setValidation("^(\\d)*$");
        maxUploadSize.setValidation("^(\\d)*$");
    }

    @UiHandler("applyButton")
    protected void handleApplySettings(ClickEvent event) {
        applySettingsHandler.run();
    }

    @UiHandler("rebuildPermissionsButton")
    protected void handleRebuildPermissions(ClickEvent event) {
        rebuildPermissionsHandler.run();
    }

    @Override
    public void setApplySettingsHandler(@Nonnull Runnable runnable) {
        this.applySettingsHandler = checkNotNull(runnable);
    }

    @Override
    public void setRebuildPermissionsHandler(@Nonnull Runnable runnable) {
        this.rebuildPermissionsHandler = checkNotNull(runnable);
    }

    @Nonnull
    @Override
    public String getApplicationName() {
        return new SafeHtmlBuilder().appendEscaped(applicationNameField.getText().trim()).toSafeHtml().asString();
    }

    @Override
    public void setApplicationName(@Nonnull String applicationName) {
        applicationNameField.setText(applicationName.trim());
    }

    @Nonnull
    @Override
    public String getSystemNotificationEmailAddress() {
        return systemNotificationEmailAddressField.getText().trim();
    }

    @Override
    public void setSystemNotificationEmailAddress(@Nonnull String emailAddress) {
        systemNotificationEmailAddressField.setText(emailAddress.trim());
    }

    @Override
    public boolean isAccountCreationAllowed() {
        return accountCreationEnabledCheckBox.getValue();
    }

    @Override
    public void setAccountCreationAllowed(boolean allowed) {
        accountCreationEnabledCheckBox.setValue(allowed);
    }

    @Override
    public boolean isProjectCreationAllowed() {
        return projectCreationEnabledCheckBox.getValue();
    }

    @Override
    public void setProjectCreationAllowed(boolean allowed) {
        projectCreationEnabledCheckBox.setValue(allowed);
    }

    @Override
    public boolean isProjectUploadAllowed() {
        return projectUploadEnabledCheckBox.getValue();
    }

    @Override
    public void setProjectUploadAllowed(boolean allowed) {
        projectUploadEnabledCheckBox.setValue(allowed);
    }

    @Override
    public boolean isNotificationEmailsEnabled() {
        return emailNotificationsEnabledCheckBox.getValue();
    }

    @Override
    public void setNotificationEmailsEnabled(boolean allowed) {
        emailNotificationsEnabledCheckBox.setValue(allowed);
    }

    @Override
    public void setScheme(@Nonnull SchemeValue scheme) {
        applicationSchemeField.setSelectedIndex(scheme.ordinal());
    }

    @Nonnull
    @Override
    public SchemeValue getScheme() {
        return SchemeValue.valueOf(applicationSchemeField.getSelectedValue());
    }

    @Override
    public void setHost(@Nonnull String host) {
        applicationHostField.setText(checkNotNull(host));
        applicationHostField.doValidation();
    }

    @Nonnull
    @Override
    public String getHost() {
        return applicationHostField.getText().trim();
    }

    @Override
    public void setPath(@Nonnull String path) {
        applicationPathField.setText(path);
        applicationPathField.doValidation();
    }

    @Nonnull
    @Override
    public String getPath() {
        return applicationPathField.getText().trim();
    }

    @Override
    public void setPort(String port) {
        applicationPortField.setText(port);
        applicationPortField.doValidation();
    }

    @Override
    public String getPort() throws NumberFormatException {
        return applicationPortField.getText().trim();
    }

    @Override
    public String getMaxUploadSize() {
        return maxUploadSize.getText().trim();
    }

    @Override
    public void setMaxUploadSize(String size) {
        maxUploadSize.setText(size);
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        tabPanel.setSize("500px", "550px");
        tabPanel.onResize();
    }
}