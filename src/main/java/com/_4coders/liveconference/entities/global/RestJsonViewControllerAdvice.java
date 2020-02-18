package com._4coders.liveconference.entities.global;

import com._4coders.liveconference.entities.account.AccountDetails;
import com._4coders.liveconference.entities.account.AccountViews;
import com._4coders.liveconference.entities.role.system.SystemRole;
import com._4coders.liveconference.entities.user.UserViews;
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
        log.atFinest().log("Setting the Json view class before request");
        Class<?> viewClass;
        if (serverHttpRequest.getURI().getPath().split("/").length < 3) {
            viewClass = AccountViews.Others.class;
        } else {
            switch (serverHttpRequest.getURI().getPath().split("/")[2].toLowerCase()) {
                case "accounts":
                    viewClass = getViewClassForAccountsEndPoint();
                    break;
                case "users":
                    viewClass = getViewClassForUsersEndPoint();
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
                viewClass = AccountViews.Admin.class;
            } else if (roles.stream().anyMatch(systemRole -> systemRole.getPermissions().stream().anyMatch(systemPermission -> systemPermission.getAction().contains("VIEW_ALL_USER_")))) {
                viewClass = AccountViews.SupportAll.class;
            } else if (roles.stream().anyMatch(systemRole -> systemRole.getPermissions().stream().anyMatch(systemPermission -> systemPermission.getAction().contains("VIEW_MEDIUM__USER_")))) {
                viewClass = AccountViews.SupportMedium.class;
            } else if (roles.stream().anyMatch(systemRole -> systemRole.getPermissions().stream().anyMatch(systemPermission -> systemPermission.getAction().contains("VIEW_LITTLE__USER_")))) {
                viewClass = AccountViews.SupportLittle.class;
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

