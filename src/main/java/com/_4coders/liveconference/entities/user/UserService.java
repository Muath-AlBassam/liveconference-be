package com._4coders.liveconference.entities.user;

import com._4coders.liveconference.entities.account.Account;
import com._4coders.liveconference.entities.account.AccountService;
import com._4coders.liveconference.entities.global.Page;
import com._4coders.liveconference.entities.setting.user.UserSetting;
import com._4coders.liveconference.entities.user.friend.FriendRepository;
import com._4coders.liveconference.exception.account.AccountNotFoundException;
import com._4coders.liveconference.exception.common.UUIDUniquenessException;
import com._4coders.liveconference.exception.sort.MappingSortPropertiesToSchemaPropertiesException;
import com._4coders.liveconference.exception.user.MaximumNumberOfUserReachedException;
import com._4coders.liveconference.exception.user.UserFoundException;
import com._4coders.liveconference.exception.user.UserNotFoundException;
import com._4coders.liveconference.util.sort.SortUtil;
import lombok.extern.flogger.Flogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@Flogger
public class UserService {
    //for now it's for all type's of users changes later
    private static final int MAXIMUM_NUMBER_OF_USERS_ALLOWED = 5;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private AccountService accountService;

    /**
     * Creates a user for the given {@code Account} with name {@code UserName}
     *
     * @param account  the {@code Account} which the {@code User} will be created for
     * @param userName the name of the to create {@code User}
     * @return the newly created {@code User}
     * @throws AccountNotFoundException            if no {@code Account} was found with the same {@code AccountId}
     * @throws UserFoundException                  if a {@code User} with the same {@code UserName} exists
     * @throws UUIDUniquenessException             if no unique {@code UUID} was found
     * @throws MaximumNumberOfUserReachedException if the MAXIMUM_NUMBER_OF_USERS_ALLOWED has been reached
     */
    public User createUserByAccount(Account account, String userName) throws AccountNotFoundException,
            UserFoundException, UUIDUniquenessException, MaximumNumberOfUserReachedException {
        log.atFinest().log("Validating the the AccountId [%d] exists", account.getId());
        if (!accountService.existAccountByIdAndIsActiveAndIsNotBlocked(account.getId())) {//shall never happen
            log.atSevere().log("AccountNotFoundException was thrown in CreateUser where it Should never happen, " +
                    "accountId [%d]", account.getId());
            throw new AccountNotFoundException(String.format("No Account with the given Id [%d] exist", account.getId()));
        } else {
            log.atFinest().log("Validating the the user has less than the maximum number [%d] of users allowed", MAXIMUM_NUMBER_OF_USERS_ALLOWED);
            if (userRepository.countUsersByAccount_IdAndIsDeletedIsFalse(account.getId()) >= MAXIMUM_NUMBER_OF_USERS_ALLOWED) {
                log.atFine().log("Throwing MaximumNumberOfUserReachedException as the maximum number of users[%d] has" +
                        "been reached", MAXIMUM_NUMBER_OF_USERS_ALLOWED);
                throw new MaximumNumberOfUserReachedException("Trying to create a new User while the " +
                        "maximum number of users has been reached", MAXIMUM_NUMBER_OF_USERS_ALLOWED, account.getId());
            } else {
                log.atFinest().log("Validating that no User with a Username same as the given [%s] exist", userName);
                if (userRepository.existsUserByUserName(userName)) {
                    log.atFine().log("UserFoundException was thrown as there exist a User with the same given UserName " +
                            "[%s]", userName);
                    throw new UserFoundException(String.format("A User with the same given UserName [%s] exists", userName));
                } else {
                    UUID uuid = generateUniqueUserUuid();
                    log.atFinest().log("Initiating User information for persistence ...");
                    User toRegister = new User();
                    toRegister.setUuid(uuid);
                    toRegister.setUserName(userName);
                    toRegister.setLastStatus(UserStatus.ONLINE);
                    toRegister.setStatus(UserStatus.OFFLINE);
                    toRegister.setIsDeleted(false);
                    toRegister.setAccount(account);
                    UserSetting userSetting = new UserSetting();
                    userSetting.setUser(toRegister);
                    toRegister.setUserSetting(userSetting);
                    log.atFinest().log("All data has been initiated, User info [%s]", toRegister);
                    log.atFinest().log("Persisting the User ...");
                    if (account.getDefaultUser() == null) {//mean no User has been created yet
                        account.setDefaultUser(toRegister);
                        account.setUsers(new HashSet<>());
                    }
                    account.getUsers().add(toRegister);
                    toRegister = userRepository.saveAndFlush(toRegister);//todo update context after account update
                    account.getUsers().add(toRegister);
                    accountService.updateAccount(account);//TODO fix shall we update the lastModified date only?
                    log.atFinest().log("User got persisted, it's info [%s]", toRegister);
                    return toRegister;
                }

            }
        }
    }

    public User getNonDeletedUserByUserNameAndAccount(Account account, String userName) {
        log.atFinest().log("Getting required User with name [%s] and Account Email [%s] and UUID [%s]",
                userName, account.getEmail(), account.getUuid());
        User fetchedUser = userRepository.getUserByUserNameAndAccount_IdAndIsDeletedIsFalse(userName, account.getId());
        log.atFinest().log("Was a User fetched from DB with the given data: [%b]", fetchedUser != null);
        if (fetchedUser == null) {
            log.atFinest().log("Throwing UserNotFoundException as there is no User with the given account id [%d] and" +
                    " userName [%s] in DB", account.getId(), userName);
            throw new UserNotFoundException("No User with the given data exists in DB", userName);
        } else {
            return fetchedUser;
        }
    }

    public Page<User> getUsersByUserName(Account requester,
                                         String toGetUserName, Pageable pageable) throws MappingSortPropertiesToSchemaPropertiesException {
        log.atFinest().log("Fetching Page<User> with name starts with [%s] and account current userName [%s] and page" +
                " [%s]", toGetUserName, requester.getCurrentInUseUser().getUserName(), pageable);
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                SortUtil.userSortMapping(pageable.getSort()));
        String orderBy =
                pageable.getSort().stream().map(order -> order.getProperty() + " " + order.getDirection()).reduce((s,
                                                                                                                   s2) -> s + ", " + s2).get();
        return new Page<>(userRepository.getUsersByUserNameStartsWith(requester.getId(), toGetUserName, orderBy,
                pageable), pageable);
    }

    /**
     * Returns the {@link User} with the given {@code userName}
     *
     * @param requesterAccount the requester {@link Account}
     * @param toGetUserName    the name of the user
     * @return the {@link User} if existing
     * @throws UserNotFoundException if no {@link User} exists with the given {@code userName}
     */
    public User getUserByUserName(Account requesterAccount, String toGetUserName) throws UserNotFoundException {
        User fetchedUser = userRepository.getUserByUserName(toGetUserName);
        if (fetchedUser == null) {
            log.atFinest().log("Throwing UserNotFoundException as there is no User with the given userName [%s]", toGetUserName);
            throw new UserNotFoundException(String.format("No User exists with the given UserName [%s]", toGetUserName),
                    toGetUserName);
        } else {
            fetchedUser.setIsFriend(friendRepository.existsFriendByAdder_UserNameAndAdded_UserNameAndIsFriendIsTrue(requesterAccount.getCurrentInUseUser().getUserName(),
                    toGetUserName));
            fetchedUser.setIsBlocked(accountService.existsBlockedAccountByBlockerId(requesterAccount.getId(),
                    fetchedUser.getAccount().getId()));
            return fetchedUser;
        }
    }//THINK check should blocked user be able to see the blocker ??

    /**
     * Returns the {@link User} with the given {@link UUID}
     *
     * @param requesterUserName the requester {@code userName}
     * @param uuid              the {@link UUID} of the user
     * @return the {@link User} if existing
     * @throws UserNotFoundException if no {@link User} exists with the given {@link UUID}
     */
    public User getUserByUUID(String requesterUserName, UUID uuid) throws UserNotFoundException {
        User fetchedUser = userRepository.getUserByUuid(uuid);
        if (fetchedUser == null) {
            log.atFinest().log("Throwing UserNotFoundException as there is no User with the given UUID [%s]", uuid);
            throw new UserNotFoundException(String.format("No User exists with the given UUID [%s]", uuid),
                    uuid);
        } else {
            fetchedUser.setIsFriend(friendRepository.existsFriendByAdder_UserNameAndAdded_UserNameAndIsFriendIsTrue(requesterUserName,
                    fetchedUser.getUserName()));
            return fetchedUser;
        }
    }//THINK check should blocked user be able to see the blocker ??


    /**
     * Returns the {@link User} with the given {@link UUID} where the {@link User} isn't deleted
     *
     * @param requesterAccount the requester {@link Account}
     * @param uuid             the {@link UUID} of the user
     * @return the {@link User} if existing
     * @throws UserNotFoundException if no {@link User} exists with the given {@link UUID}
     */
    public User getUserByUUIDAndIsDeletedIsFalse(Account requesterAccount, UUID uuid) throws UserNotFoundException {
        User fetchedUser = userRepository.getUserByUuidAndIsDeletedIsFalse(uuid);
        if (fetchedUser == null) {
            log.atFinest().log("Throwing UserNotFoundException as there is no User with the given UUID [%s]", uuid);
            throw new UserNotFoundException(String.format("No User exists with the given UUID [%s]", uuid),
                    uuid);
        } else {
            fetchedUser.setIsFriend(friendRepository.existsFriendByAdder_UserNameAndAdded_UserNameAndIsFriendIsTrue(requesterAccount.getCurrentInUseUser().getUserName(),
                    fetchedUser.getUserName()));
            fetchedUser.setIsBlocked(accountService.existsBlockedAccountByBlockerId(requesterAccount.getId(),
                    fetchedUser.getAccount().getId()));
            return fetchedUser;
        }
    }//THINK check should blocked user be able to see the blocker ??
    //todo check after chat and message has been implemented if any thing changes

    /**
     * Deletes a {@code User} with the give {@code UserName} from the {@code Account} with {@code ID}
     *
     * @param accountId the {@code Account} owner of the to delete {@code User}
     * @param userName  the name of the {@code User} to delete
     * @return true if the {@code User} was deleted
     * @throws AccountNotFoundException if no {@code Account} was found with the same {@code AccountId}
     * @throws UserNotFoundException    if the given {@code Account ID} has no {@code User} with the given {@code UserName}
     */
    public boolean deleteUserByUserNameAndAccountId(Long accountId, String userName) throws AccountNotFoundException, UserNotFoundException {
        log.atFinest().log("Initiating User deletion with AccountId [%d] and UserName [%s]", accountId, userName);
        log.atFinest().log("Validating that the given AccountId [%d] exists", accountId);
        if (!accountService.existAccountByIdAndIsActiveAndIsNotBlocked(accountId)) {//shall never happen
            log.atSevere().log("AccountNotFoundException was thrown in deleteUserByUserNameAndAccountId where it Should never happen, " +
                    "accountId [%d]", accountId);
            throw new AccountNotFoundException(String.format("No Account with the given Id [%d] exist", accountId));
        } else {
            log.atFinest().log("Checking wither the given AccountId [%d] has a User with the same UserName [%s]",
                    accountId, userName);
            User fetchedUser = userRepository.getUserByAccount_IdAndIsDeletedIsFalse(accountId);
            if (fetchedUser == null) {
                log.atFine().log("The given Account with Id [%d] has no User with UserName [%s] to delete", accountId, userName);
                throw new UserNotFoundException(String.format("No User was found for the Account with Id [%d] and the " +
                        "UserName [%s]", accountId, userName), userName);
            } else {
                log.atFinest().log("Setting isDeleted to [%b]", true);
                fetchedUser.setIsDeleted(true);
                log.atFinest().log("Starting the deletion process ...");
                userRepository.saveAndFlush(fetchedUser);
                return true;
            }
        }
    }

    /**
     * Deletes a {@code User} with the give {@code UserName}
     *
     * @param userName the name of the {@code User} to delete
     * @return true if the {@code User} was deleted
     * @throws UserNotFoundException when no unDeleted {@code User} exists with the given {@code UserName}
     */
    public boolean deleteUserByUserName(String userName) throws AccountNotFoundException, UserNotFoundException {
        log.atFinest().log("Initiating User deletion with UserName [%s]", userName);
        log.atFinest().log("Checking that there exist a User with the given UserName [%s] and isn't deleted", userName);
        User fetchedUser = userRepository.getUserByUserNameAndIsDeletedIsFalse(userName);
        if (fetchedUser == null) {
            log.atFine().log("The given UserName wasn't found in DB either no users exist with such name or it's " +
                    "already deleted", userName);
            throw new UserNotFoundException(String.format("No User was found with UserName [%s] and isn't delete", userName), userName);
        } else {
            log.atFinest().log("Setting isDeleted to [%b]", true);
            fetchedUser.setIsDeleted(true);
            log.atFinest().log("Starting the deletion process ...");
            userRepository.saveAndFlush(fetchedUser);
            return true;
        }
    }

    /**
     * Deletes a {@code User} with the give {@code UserUuid}
     *
     * @param uuid the {@code UUID} of the {@code User} to delete
     * @return true if the {@code User} was deleted
     * @throws UserNotFoundException when no unDeleted {@code User} exists with the given {@code UUID}
     */
    public boolean deleteUserByUserUuid(UUID uuid) throws AccountNotFoundException, UserNotFoundException {
        log.atFinest().log("Initiating User deletion with UUID [%s]", uuid);
        log.atFinest().log("Checking that there exist a User with the given UUID [%s] and isn't deleted", uuid);
        User fetchedUser = userRepository.getUserByUuidAndIsDeletedIsFalse(uuid);
        if (fetchedUser == null) {
            log.atFine().log("The given UUID wasn't found in DB either no users exist with such UUID or it's " +
                    "already deleted", uuid);
            throw new UserNotFoundException(String.format("No User was found with UUID [%s] and isn't delete", uuid), uuid);
        } else {
            log.atFinest().log("Setting isDeleted to [%b]", true);
            fetchedUser.setIsDeleted(true);
            log.atFinest().log("Starting the deletion process ...");
            userRepository.saveAndFlush(fetchedUser);
            return true;
        }
    }

    /**
     * Updates a {@code User} {@code UserName} from the given {@code AccountID} and {@code User} {@code UUID}
     *
     * @param accountId the {@code ID} of the owner {@code Account} of the {@code User} to update
     * @param userUuid  the {@code UUID} of the {@code User} to update
     * @param userName  the new name for the {@code User} to update
     * @return the updated {@code User}
     * @throws AccountNotFoundException if the given {@code ID} was not related to any {@code Account}
     * @throws UserNotFoundException    if the given {@code User} {@code UUID} wasn't found in relation with the given
     *                                  {@code Account} {@code ID}
     * @throws UserFoundException       if there exist a {@code User} with the same given {@code UserName}
     */
    public User updateUserNameByUserUuid(Long accountId, UUID userUuid, String userName) throws AccountNotFoundException, UserNotFoundException, UserFoundException {
        log.atFinest().log("Initiating UserName updating with AccountId [%d], UserUUID [%s] and UserName [%s]",
                accountId, userUuid, userName);
        log.atFinest().log("Validating that the given AccountId [%d] exists", accountId);
        if (!accountService.existAccountByIdAndIsActiveAndIsNotBlocked(accountId)) {//shall never happen
            log.atSevere().log("AccountNotFoundException was thrown in updateUserNameByUserUuid where it Should never happen, " +
                    "accountId [%d]", accountId);
            throw new AccountNotFoundException(String.format("No Account with the given Id [%d] exist", accountId));
        } else {
            log.atFinest().log("Validating the no User exist's with the given UserName [%s] and not deleted", userName);
            if (userRepository.existsUserByUserName(userName)) {
                log.atFine().log("User was found with the same give UserName [%s]", userName);
                throw new UserFoundException(String.format("User was found with the same given UserName [%s]", userName),
                        userName);
            } else {
                log.atFinest().log("Validating the a User with the given UUID exist ...");
                User fetchedUser = userRepository.getUserByUuidAndAccount_IdAndIsDeletedIsFalse(userUuid, accountId);
                if (fetchedUser == null) {
                    log.atFine().log("The given UUID [%s] wasn't found with the given Account ID [%d] in DB either no " +
                            "users exist with such relation or it's deleted", userUuid, accountId);
                    throw new UserNotFoundException(String.format("No User was found with UUID [%s] and Account ID [%d] " +
                            "relation and isn't delete", userUuid, accountId));
                } else {
                    log.atFinest().log("User got fetched from DB");
                    log.atFinest().log("Setting the new UserName");
                    fetchedUser.setUserName(userName);
                    log.atFinest().log("Updating the User");
                    return userRepository.saveAndFlush(fetchedUser);
                }
            }

        }
    }

    /**
     * Updates a {@code User} {@code Status} from the given {@code AccountID} and {@code User} {@code UUID}
     *
     * @param accountId the {@code ID} of the owner {@code Account} of the {@code User} to update
     * @param userUuid  the {@code UUID} of the {@code User} to update
     * @param status    the new {@code Status} for the {@code User} to update
     * @return true if the {@code Status} was updated
     * @throws AccountNotFoundException if the given {@code ID} was not related to any {@code Account}
     * @throws UserNotFoundException    if the given {@code User} {@code UUID} wasn't found in relation with the given
     *                                  {@code Account} {@code ID}
     * @throws IllegalArgumentException if the given {@code Status} is unknown
     */
    public boolean updateUserStatusByUserUuid(Long accountId, UUID userUuid, String status) throws AccountNotFoundException
            , UserNotFoundException, IllegalArgumentException {
        log.atFinest().log("Initiating User status updating with AccountId [%d], UserUUID [%s] and status [%s]",
                accountId, userUuid, status);
        log.atFinest().log("Validating that the given AccountId [%d] exists", accountId);
        if (!accountService.existAccountByIdAndIsActiveAndIsNotBlocked(accountId)) {//shall never happen
            log.atSevere().log("AccountNotFoundException was thrown in updateUserStatusByUserUuid where it Should never happen, " +
                    "accountId [%d]", accountId);
            throw new AccountNotFoundException(String.format("No Account with the given Id [%d] exist", accountId));
        } else {
            log.atFinest().log("Validating the a User with the given UUID exist ...");
            User fetchedUser = userRepository.getUserByUuidAndAccount_IdAndIsDeletedIsFalse(userUuid, accountId);
            if (fetchedUser == null) {
                log.atFine().log("The given UUID [%s] wasn't found with the given Account ID [%d] in DB either no " +
                        "users exist with such relation or it's deleted", userUuid, accountId);
                throw new UserNotFoundException(String.format("No User was found with UUID [%s] and Account ID [%d] " +
                        "relation and isn't delete", userUuid, accountId));
            } else {
                log.atFinest().log("User got fetched from DB");
                log.atFinest().log("Setting the new User status");
                UserStatus userStatus = UserStatus.valueOf(status.toUpperCase());
                fetchedUser.setStatus(userStatus);
                log.atFinest().log("Updating the User");
                userRepository.saveAndFlush(fetchedUser);
                return true;
            }
        }


    }

    /**
     * Getting non deleted {@code Set of Users} by given {@code email}
     *
     * @param email the email to look for {@code users} with
     * @return {@code Set of Users} by given {@code email} otherwise null
     */
    public Set<User> getNonDeletedUsersByEmail(String email, Sort sort) throws MappingSortPropertiesToSchemaPropertiesException {
        log.atFinest().log("Getting Set of Users by Email [%s] and Sort [%s]", email, sort);
        log.atFinest().log("Validating that an Account exist with the given Email [%s]", email);
        if (!accountService.existAccountByEmail(email)) {
            log.atFinest().log("No Account exist with the given Email [%s]", email);
            throw new AccountNotFoundException(String.format("No Account was found with the given Email [%s]", email), email);
        } else {
            log.atFinest().log("Checking that Sort is sorted or not and if not getting the default sort");
            sort = SortUtil.userSortMapping(sort);
            log.atFinest().log("Fetching Users...");
            Set<User> toReturn = userRepository.getUsersByAccount_EmailAndIsDeletedIsFalse(email, sort);
            log.atFinest().log("Result of Users fetching is [%s]", toReturn);
            return toReturn;
        }
    }

    /**
     * Getting non deleted {@code Set of Users} by given {@code UUID}
     *
     * @param uuid the {@code UUID} to look for {@code users} with
     * @return {@code Set of Users} by given {@code UUID} otherwise null
     */
    public Set<User> getNonDeletedUsersByUuid(UUID uuid, Sort sort) throws MappingSortPropertiesToSchemaPropertiesException {
        log.atFinest().log("Getting Set of Users by Uuid [%s] and Sort [%s]", uuid, sort);
        log.atFinest().log("Validating that an Account exist with the given UUID [%s]", uuid);
        if (!accountService.existAccountByUuid(uuid)) {
            log.atFinest().log("No Account exist with the given UUID [%s]", uuid);
            throw new AccountNotFoundException(String.format("No Account was found with the given UUID [%s]", uuid),
                    uuid);
        } else {
            log.atFinest().log("Checking that Sort is sorted or not and if not getting the default sort");
            sort = SortUtil.userSortMapping(sort);
            log.atFinest().log("Fetching Users...");
            Set<User> toReturn = userRepository.getUsersByAccount_UuidAndIsDeletedIsFalse(uuid, sort);
            log.atFinest().log("Result of Users fetching is [%s]", toReturn);
            return toReturn;
        }
    }

    /**
     * Getting non deleted {@code Set of Users} by given {@code AccountId}
     *
     * @param accountId the {@code AccountId} to look for {@code users} with
     * @return {@code Set of Users} by given {@code accountId} otherwise null
     */
    public Set<User> getNonDeletedUsersByAccountId(Long accountId, Sort sort) throws MappingSortPropertiesToSchemaPropertiesException {
        log.atFinest().log("Getting Set of Users by accountId [%d] and Sort [%s]", accountId, sort);
        if (!accountService.existAccountById(accountId)) {
            log.atFinest().log("No Account exist with the given ID [%s]", accountId);
            throw new AccountNotFoundException(String.format("No Account was found with the given ID [%s]", accountId),
                    accountId);
        } else {
            log.atFinest().log("Checking that Sort is sorted or not and if not getting the default sort");
            sort = SortUtil.userSortMapping(sort);
            log.atFinest().log("Fetching Users...");
            Set<User> toReturn = userRepository.getUsersByAccount_IdAndIsDeletedIsFalse(accountId, sort);
            log.atFinest().log("Result of Users fetching is [%s]", toReturn);
            return toReturn;
        }
    }

    /**
     * Returns {@code boolean} if a {@code User} exist with the  given {@code UUID}
     *
     * @param uuid the {@code UUID} to lock for
     * @return true if a {@code User} with given {@code UUID} was found, false otherwise
     */
    public boolean existUserByUuid(UUID uuid) {
        log.atFinest().log("Checking wither a User with the given UUID [%s] exist", uuid);
        final boolean toReturn = userRepository.existsUserByUuid(uuid);
        log.atFinest().log("Result of User existence checking [%b]", toReturn);
        return toReturn;
    }

    /**
     * Generates unique {@code UUID} for a user
     *
     * @return unique {@code UUID} in domain of {@code Users}
     * @throws UUIDUniquenessException if no unique {@code UUID} was found
     */
    private UUID generateUniqueUserUuid() throws UUIDUniquenessException {
        log.atFinest().log("Getting unique UUID value ...");
        byte counterLimit = 0;
        UUID uniqueUUID = UUID.randomUUID();
        while (existUserByUuid(uniqueUUID) && counterLimit >= 0) {
            uniqueUUID = UUID.randomUUID();
            counterLimit++;//play with overflow
            log.atFinest().every(16).log("[%d] iterations has gone from 128 to find unique UUID", counterLimit + 1);
        }
        if (counterLimit < 0) {// no unique UUID was found
            log.atSevere().log("Throwing the exception [$s]", UUIDUniquenessException.class);
            throw new UUIDUniquenessException(String.format("No unique UUID was found after [%d] tries", counterLimit),
                    counterLimit);
        } else {
            log.atFinest().log("Unique UUID was found [%s]", uniqueUUID);
            return uniqueUUID;
        }
    }

}
