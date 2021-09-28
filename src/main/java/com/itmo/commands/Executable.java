package com.itmo.commands;

import com.itmo.app.Application;
import com.itmo.client.User;

/**
 * Command template
 */
public interface Executable {
    String execute(Application application, User user);
}
