package com._4coders.liveconference.entities.global;

import com._4coders.liveconference.entities.account.AccountDetails;
import com._4coders.liveconference.entities.account.AccountViews;
import com._4coders.liveconference.entities.conference.ConferenceViews;
import com._4coders.liveconference.entities.role.system.SystemRole;
import com._4coders.liveconference.entities.user.UserViews;
import com._4coders.liveconference.entities.user.friend.FriendView;
import lombok.extern.flogger.Flogger;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;

import java.util.Set;

@RestControllerAdvice
@Flogger
public class RestJsonViewControllerAdvice extends AbstractMappingJacksonResponseBodyAdvice {
    /*AccountViews.Owner should be set with @JsonView*/
    @Override
    protected void beforeBodyWriteInternal(MappingJacksonValue mappingJacksonValue, MediaType mediaType, MethodParameter methodParameter,
                                           ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        //TODO CHECK for incoming request target and set the correct view as each entity has it's own views
        log.atFinest().log("Setting the Json view class before returning data");
        Class<?> viewClass;
        final String[] splittedPath = serverHttpRequest.getURI().getPath().split("/");
        if (splittedPath.length < 3) {
            viewClass = AccountViews.Others.class;
        } else {
            switch (splittedPath[2].toLowerCase()) {
                case "accounts":
                    viewClass = getViewClassForAccountsEndPoint();
                    break;
                case "users":
                    if (splittedPath.length >= 4 && splittedPath[3].equalsIgnoreCase("friends")) {
                        viewClass = getViewClassForFriendsEndPoint();
                    } else {
                        viewClass = getViewClassForUsersEndPoint();
                    }
                    break;
                case "o_conference":
                    viewClass = getViewClassForOConferenceEndPoint();
                    break;
                default:
                    viewClass = AccountViews.Others.class;
            }
        }


        log.atFinest().log("View should be [%s]", viewClass);
        mappingJacksonValue.setSerializationView(viewClass);
    }

    private Class<?> getViewClassForUsersEndPoint() {
        Class<?> viewClass = UserViews.Others.class;
        Set<SystemRole> roles = getRoles();
        if (roles != null) {
            if (roles.stream().anyMatch(systemRole -> systemRole.getPermissions().stream().anyMatch(systemPermission -> systemPermission.getAction().contains("_ADMIN")))) {
                viewClass = UserViews.Admin.class;
            } else if (roles.stream().anyMatch(systemRole -> systemRole.getPermissions().stream().anyMatch(systemPermission -> systemPermission.getAction().contains("VIEW_ALL_USER_")))) {
                viewClass = UserViews.SupportAll.class;
            } else if (roles.stream().anyMatch(systemRole -> systemRole.getPermissions().stream().anyMatch(systemPermission -> systemPermission.getAction().contains("VIEW_MEDIUM__USER_")))) {
                viewClass = UserViews.SupportMedium.class;
            } else if (roles.stream().anyMatch(systemRole -> systemRole.getPermissions().stream().anyMatch(systemPermission -> systemPermission.getAction().contains("VIEW_LITTLE__USER_")))) {
                viewClass = UserViews.SupportLittle.class;
            }
        }
        return viewClass;
    }

    private Class<?> getViewClassForFriendsEndPoint() {
        Class<?> viewClass = FriendView.Others.class;
        Set<SystemRole> roles = getRoles();
        if (roles != null) {
            if (roles.stream().anyMatch(systemRole -> systemRole.getPermissions().stream().anyMatch(systemPermission -> systemPermission.getAction().contains("_ADMIN")))) {
                viewClass = FriendView.Admin.class;
            } else if (roles.stream().anyMatch(systemRole -> systemRole.getPermissions().stream().anyMatch(systemPermission -> systemPermission.getAction().contains("VIEW_ALL_USER_")))) {
                viewClass = FriendView.SupportAll.class;
            } else if (roles.stream().anyMatch(systemRole -> systemRole.getPermissions().stream().anyMatch(systemPermission -> systemPermission.getAction().contains("VIEW_MEDIUM__USER_")))) {
                viewClass = FriendView.SupportMedium.class;
            } else if (roles.stream().anyMatch(systemRole -> systemRole.getPermissions().stream().anyMatch(systemPermission -> systemPermission.getAction().contains("VIEW_LITTLE__USER_")))) {
                viewClass = FriendView.SupportLittle.class;
            }
        }
        return viewClass;
    }

    private Class<?> getViewClassForOConferenceEndPoint() {
        Class<?> viewClass = ConferenceViews.Others.class;
        Set<SystemRole> roles = getRoles();
        if (roles != null) {
            if (roles.stream().anyMatch(systemRole -> systemRole.getPermissions().stream().anyMatch(systemPermission -> systemPermission.getAction().contains("_ADMIN")))) {
                viewClass = ConferenceViews.Admin.class;
            } else if (roles.stream().anyMatch(systemRole -> systemRole.getPermissions().stream().anyMatch(systemPermission -> systemPermission.getAction().contains("VIEW_ALL_USER_")))) {
                viewClass = ConferenceViews.SupportAll.class;
            } else if (roles.stream().anyMatch(systemRole -> systemRole.getPermissions().stream().anyMatch(systemPermission -> systemPermission.getAction().contains("VIEW_MEDIUM__USER_")))) {
                viewClass = ConferenceViews.SupportMedium.class;
            } else if (roles.stream().anyMatch(systemRole -> systemRole.getPermissions().stream().anyMatch(systemPermission -> systemPermission.getAction().contains("VIEW_LITTLE__USER_")))) {
                viewClass = ConferenceViews.SupportLittle.class;
            }
        }
        return viewClass;
    }

    private Set<SystemRole> getRoles() {
        if (SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null &&
                SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetails) {
            return ((AccountDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAccount().getRoles();
        }
        return null;
    }


    private Class<?> getViewClassForAccountsEndPoint() {
        Class<?> viewClass = AccountViews.Others.class;
        Set<SystemRole> roles = getRoles();
        if (roles != null) {
            if (roles.stream().anyMatch(systemRole -> systemRole.getPermissions().stream().anyMatch(systemPermission -> systemPermission.getAction().contains("_ADMIN")))) {
                viewClass = AccountViews.Admin.class;
            } else if (roles.stream().anyMatch(systemRole -> systemRole.getPermissions().stream().anyMatch(systemPermission -> systemPermission.getAction().contains("VIEW_ALL_ACCOUNT_")))) {
                viewClass = AccountViews.SupportAll.class;
            } else if (roles.stream().anyMatch(systemRole -> systemRole.getPermissions().stream().anyMatch(systemPermission -> systemPermission.getAction().contains("VIEW_MEDIUM__ACCOUNT_")))) {
                viewClass = AccountViews.SupportMedium.class;
            } else if (roles.stream().anyMatch(systemRole -> systemRole.getPermissions().stream().anyMatch(systemPermission -> systemPermission.getAction().contains("VIEW_LITTLE__ACCOUNT_")))) {
                viewClass = AccountViews.SupportLittle.class;
            }
        }
        return viewClass;
    }
}

