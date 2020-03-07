package com._4coders.liveconference.entities.global;

import com._4coders.liveconference.entities.account.AccountViews;
import com._4coders.liveconference.entities.account.accountIpAddress.AccountIpAddressViews;
import com._4coders.liveconference.entities.account.blockedAccount.BlockedAccountViews;
import com._4coders.liveconference.entities.account.systemBlocked.SystemBlockedAccountViews;
import com._4coders.liveconference.entities.address.AddressViews;
import com._4coders.liveconference.entities.category.CategoryViews;
import com._4coders.liveconference.entities.channel.ChannelViews;
import com._4coders.liveconference.entities.chat.ChatViews;
import com._4coders.liveconference.entities.conference.ConferenceViews;
import com._4coders.liveconference.entities.group.GroupViews;
import com._4coders.liveconference.entities.ipAddress.IpAddressViews;
import com._4coders.liveconference.entities.message.MessageViews;
import com._4coders.liveconference.entities.permission.PermissionViews;
import com._4coders.liveconference.entities.role.RoleViews;
import com._4coders.liveconference.entities.setting.SettingViews;
import com._4coders.liveconference.entities.user.UserViews;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Page implementation for the JsonView issue (so that we can see the page in the sent json)
 *
 * @author Abdulmajid
 * @version 0.0.1
 * @since 06/03/2020
 */
@JsonView({AccountViews.OwnerDetails.class, AccountViews.OwnerInformation.class, AccountViews.Others.class,
        AccountIpAddressViews.OwnerDetails.class, AccountIpAddressViews.OwnerInformation.class,
        AccountIpAddressViews.SupportLittle.class,
        BlockedAccountViews.OwnerDetails.class, BlockedAccountViews.OwnerInformation.class,
        BlockedAccountViews.Others.class,
        SystemBlockedAccountViews.OwnerDetails.class, SystemBlockedAccountViews.OwnerInformation.class,
        SystemBlockedAccountViews.Others.class,
        AddressViews.OwnerDetails.class, AddressViews.OwnerInformation.class, AddressViews.SupportLittle.class,
        CategoryViews.OwnerDetails.class, CategoryViews.OwnerInformation.class, CategoryViews.SupportLittle.class,
        ChannelViews.OwnerDetails.class, ChannelViews.OwnerInformation.class, ChannelViews.Others.class,
        ChatViews.OwnerDetails.class, ChatViews.OwnerInformation.class, ChatViews.Others.class,
        ConferenceViews.OwnerDetails.class, ConferenceViews.OwnerInformation.class, ConferenceViews.Others.class,
        GroupViews.OwnerDetails.class, GroupViews.OwnerInformation.class, GroupViews.Others.class,
        IpAddressViews.OwnerDetails.class, IpAddressViews.OwnerInformation.class, IpAddressViews.Others.class,
        MessageViews.OwnerDetails.class, MessageViews.OwnerInformation.class, MessageViews.Others.class,
        PermissionViews.OwnerDetails.class, PermissionViews.OwnerInformation.class, PermissionViews.SupportLittle.class,
        RoleViews.OwnerDetails.class, RoleViews.OwnerInformation.class, RoleViews.Others.class,
        SettingViews.OwnerDetails.class, SettingViews.OwnerInformation.class, SettingViews.Others.class,
        UserViews.OwnerDetails.class, UserViews.OwnerInformation.class, UserViews.Others.class})
public class Page<T> extends org.springframework.data.domain.PageImpl<T> {
    public Page(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public Page(List<T> content) {
        super(content);
    }

    public Page(org.springframework.data.domain.Page<T> page, Pageable pageable) {
        this(page.getContent(), pageable, page.getTotalElements());
    }

    @Override
    public int getTotalPages() {
        return super.getTotalPages();
    }

    @Override
    public long getTotalElements() {
        return super.getTotalElements();
    }

    @Override
    public boolean hasNext() {
        return super.hasNext();
    }

    @Override
    public boolean isLast() {
        return super.isLast();
    }

    @Override
    public List<T> getContent() {
        return super.getContent();
    }

    @Override
    public boolean hasContent() {
        return super.hasContent();
    }
}
