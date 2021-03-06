package edu.stanford.bmir.protege.web.client;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import edu.stanford.bmir.protege.web.client.app.ApplicationPresenter;
import edu.stanford.bmir.protege.web.client.app.ApplicationView;
import edu.stanford.bmir.protege.web.client.app.WebProtegeInitializer;
import edu.stanford.bmir.protege.web.client.inject.WebProtegeClientInjector;
import edu.stanford.bmir.protege.web.client.place.WebProtegeActivityManager;
import edu.stanford.protege.widgetmap.resources.WidgetMapClientBundle;

import static edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle.BUNDLE;


/**
 * @author Matthew Horridge
 */
public class WebProtege implements EntryPoint {

    public void onModuleLoad() {

        WebProtegeInitializer initializer = WebProtegeClientInjector.get().getWebProtegeInitializer();
        initializer.init(new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                GWT.log("There was a problem initializing WebProtege", caught);
            }

            @Override
            public void onSuccess(Void result) {
                GWT.log("Application initialization complete.  Starting UI Initialization.");
                handleUIInitialization();
            }
        });
    }

    private void handleUIInitialization() {
        buildUI();
    }

    private void buildUI() {

        BUNDLE.laf().ensureInjected();
        BUNDLE.toolbar().ensureInjected();
        BUNDLE.login().ensureInjected();
        BUNDLE.style().ensureInjected();
        BUNDLE.buttons().ensureInjected();
        BUNDLE.discussion().ensureInjected();
        BUNDLE.menu().ensureInjected();
        BUNDLE.settings().ensureInjected();
        BUNDLE.valueList().ensureInjected();
        BUNDLE.tags().ensureInjected();
        WidgetMapClientBundle.BUNDLE.style().ensureInjected();

        ApplicationPresenter applicationPresenter = WebProtegeClientInjector.get().getApplicationPresenter();
        applicationPresenter.start();

        ApplicationView applicationView = applicationPresenter.getApplicationView();

        RootLayoutPanel.get().add(applicationView);

        WebProtegeActivityManager activityManager =  WebProtegeClientInjector.get().getActivityManager();
        activityManager.setDisplay(applicationView);

        PlaceHistoryHandler placeHistoryHandler = WebProtegeClientInjector.get().getPlaceHistoryHandler();
        placeHistoryHandler.handleCurrentHistory();


    }

}
