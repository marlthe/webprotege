package edu.stanford.bmir.protege.web.client.watches;

import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.change.ChangeListView;
import edu.stanford.bmir.protege.web.client.change.ChangeListViewImpl;
import edu.stanford.bmir.protege.web.client.change.ChangeListViewPresenter;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;

public class WatchedEntitiesPortlet extends AbstractOWLEntityPortlet {

    private final DispatchServiceManager dispatchServiceManager;

    private ChangeListView changeListView;

    private final LoggedInUserProvider loggedInUserProvider;

    @Inject
    public WatchedEntitiesPortlet(SelectionModel selectionModel, EventBus eventBus, DispatchServiceManager dispatchServiceManager, ProjectId projectId, LoggedInUserProvider loggedInUserProvider) {
        super(selectionModel, eventBus, projectId, loggedInUserProvider);
        this.dispatchServiceManager = dispatchServiceManager;
        this.loggedInUserProvider = loggedInUserProvider;
    }

    @Override
    public void initialize() {
        setHeight(200);
        changeListView = new ChangeListViewImpl();
        ScrollPanel scrollPanel = new ScrollPanel(changeListView.asWidget());
        scrollPanel.setWidth("100%");
        scrollPanel.setHeight("100%");
        add(scrollPanel);
        onRefresh();
    }


    @Override
    public void onLogin(UserId userId) {
        onRefresh();
    }

    @Override
    public void onLogout(UserId userId) {
        onRefresh();
    }

    @Override
    protected void onRefresh() {
        ChangeListViewPresenter presenter = new ChangeListViewPresenter(changeListView, dispatchServiceManager, false);
        presenter.setChangesForWatches(getProjectId(), getUserId());
        setTitle(generateTitle());
    }

    private String generateTitle() {
        return "Watched Entities " + (loggedInUserProvider.getCurrentUserId().isGuest() ? " - Sign in to see the watched entities" : " for " + loggedInUserProvider.getCurrentUserId().getUserName());
    }
}
