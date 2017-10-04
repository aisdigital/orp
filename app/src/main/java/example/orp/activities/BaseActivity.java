package example.orp.activities;

import com.github.aistech.orp.activities.ORPActivity;
import com.github.aistech.orp.annotations.Extra;

import example.orp.model.User;

/**
 * Created by jonathan on 06/01/17.
 */

public abstract class BaseActivity extends ORPActivity {

    @Extra("user")
    protected User firstUser;

}
