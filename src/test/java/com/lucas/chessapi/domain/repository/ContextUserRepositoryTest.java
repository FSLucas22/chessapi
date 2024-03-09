package com.lucas.chessapi.domain.repository;

import com.lucas.chessapi.domain.RepositoryContextHelper;
import com.lucas.chessapi.model.UserEntity;
import com.lucas.chessapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class ContextUserRepositoryTest extends RepositoryContextHelper {
    @Autowired
    private UserRepository repository;
    private Optional<UserEntity> returnedUser;
    private Page<UserEntity> returnedUserPage;

    protected void whenFindByEmail(String email) {
        returnedUser = repository.findByEmail(email);
    }

    protected void whenUserIsSaved(UserEntity user) {
        try {
            repository.save(user);
        } catch (Exception e) {
            error = e;
        }
    }

    protected void whenFindAllIsCalledWith(PageRequest pagination) {
        try {
            returnedUserPage = repository.findAll(pagination);
        } catch (Exception e) {
            error = e;
        }
    }

    protected void thenReturnedUserShouldBeEqual(Optional<UserEntity> possibleUser) {
        assertThat(returnedUser)
                .usingRecursiveComparison()
                .isEqualTo(possibleUser);
    }

    protected void thenReturnedPageShoudBe(Page<UserEntity> userPage) {
        assertThat(returnedUserPage)
                .usingRecursiveComparison()
                .isEqualTo(userPage);
    }
}
