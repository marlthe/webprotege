package edu.stanford.bmir.protege.web.server.tag;

import edu.stanford.bmir.protege.web.server.persistence.Repository;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.tag.Tag;
import edu.stanford.bmir.protege.web.shared.tag.TagId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.server.tag.EntityTags.*;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15 Mar 2018
 */
public class EntityTagsRepository implements Repository {

    @Nonnull
    private final Datastore datastore;

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final Lock readLock = readWriteLock.readLock();

    private final Lock writeLock = readWriteLock.writeLock();

    @Inject
    public EntityTagsRepository(@Nonnull Datastore datastore) {
        this.datastore = checkNotNull(datastore);
    }

    @Override
    public void ensureIndexes() {
        datastore.ensureIndexes(EntityTags.class);
    }

    public void save(EntityTags tag) {
        try {
            writeLock.lock();
            datastore.delete(tagWithProjectIdAndEntity(tag.getProjectId(), tag.getEntity()));
            datastore.save(tag);
        } finally {
            writeLock.unlock();
        }
    }

    public void addTag(ProjectId projectId, OWLEntity entity, TagId tagId) {
        try {
            writeLock.lock();
            Query<EntityTags> query = tagWithProjectIdAndEntity(projectId, entity);
            UpdateOperations<EntityTags> updateOps = datastore.createUpdateOperations(EntityTags.class);
            updateOps.addToSet(TAGS, tagId);
            datastore.update(query, updateOps);
        } finally {
            writeLock.unlock();
        }
    }

    public void removeTag(ProjectId projectId, OWLEntity entity, TagId tagId) {
        try {
            writeLock.lock();
            Query<EntityTags> query = tagWithProjectIdAndEntity(projectId, entity);
            UpdateOperations<EntityTags> updateOps = datastore.createUpdateOperations(EntityTags.class);
            updateOps.removeAll(TAGS, tagId);
            datastore.update(query, updateOps);
        } finally {
            writeLock.unlock();
        }
    }

    public void removeTag(ProjectId projectId, TagId tagId) {
        try {
            writeLock.lock();
            Query<EntityTags> query = datastore.createQuery(EntityTags.class)
                                               .field(PROJECT_ID).equal(projectId);
            UpdateOperations<EntityTags> updateOps = datastore.createUpdateOperations(EntityTags.class);
            updateOps.removeAll(TAGS, tagId);
            datastore.update(query, updateOps);
        } finally {
            writeLock.unlock();
        }
    }

    private Query<EntityTags> tagWithProjectIdAndEntity(ProjectId projectId, OWLEntity entity) {
        return datastore.createQuery(EntityTags.class)
                        .field(PROJECT_ID).equal(projectId)
                        .field(ENTITY).equal(entity);
    }

    public Map<OWLEntity, EntityTags> findByProject(ProjectId projectId) {
        try {
            readLock.lock();
            return datastore.createQuery(EntityTags.class)
                            .field(PROJECT_ID).equal(projectId)
                            .asList()
                            .stream()
                            .collect(toMap(EntityTags::getEntity, tags -> tags));
        } finally {
            readLock.unlock();
        }
    }

    public Optional<EntityTags> findByEntity(ProjectId projectId, OWLEntity entity) {
        try {
            readLock.lock();
            return Optional.ofNullable(tagWithProjectIdAndEntity(projectId, entity).get());
        } finally {
            readLock.unlock();
        }
    }

    public Collection<EntityTags> findByTagId(TagId tagId) {
        try {
            readLock.lock();
            return datastore.find(EntityTags.class)
                            .field(TAGS)
                            .equal(tagId)
                            .asList();
        } finally {
            readLock.unlock();
        }
    }

}
