package com.pccw.backend.repository;

import com.pccw.backend.entity.DbResAccount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResAccountRepository extends BaseRepository<DbResAccount> {

    DbResAccount getDbResAccountsByAccountNameAndAccountPassword(String accountName, String accountPassword);

    DbResAccount findDbResAccountByAccountName(String name);

    DbResAccount findDbResAccountById(Long id);

    @Query(value = "SELECT REPO_ID FROM RES_ACCOUNT_REPO WHERE ACCOUNT_ID=?1",nativeQuery = true)
    List<Long> findRepoByAccountId(Long id);
}
