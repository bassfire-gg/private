package org.user_service.assembler;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.user_service.controller.UserController;
import org.user_service.response.UserResponse;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<UserResponse, EntityModel<UserResponse>> {

    public static final String USERS_REL = "users";
    public static final String UPDATE_REL = "update";
    public static final String DELETE_REL = "delete";

    @Override
    public EntityModel<UserResponse> toModel(UserResponse user) {
        EntityModel<UserResponse> model = EntityModel.of(user);

        model.add(linkTo(methodOn(UserController.class).getUser(user.getId())).withSelfRel());
        model.add(linkTo(methodOn(UserController.class).getUsers()).withRel(USERS_REL));
        model.add(linkTo(methodOn(UserController.class).updateUser(user.getId(), null)).withRel(UPDATE_REL));
        model.add(linkTo(UserController.class).slash(user.getId()).withRel(DELETE_REL));

        return model;
    }

    @Override
    public CollectionModel<EntityModel<UserResponse>> toCollectionModel(Iterable<? extends UserResponse> users) {
        CollectionModel<EntityModel<UserResponse>> collection = RepresentationModelAssembler.super.toCollectionModel(users);
        collection.add(linkTo(methodOn(UserController.class).getUsers()).withSelfRel());
        collection.add(linkTo(methodOn(UserController.class).createUser(null)).withRel("create"));
        return collection;
    }
}
