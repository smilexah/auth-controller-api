package sdu.edu.kz.authcontroller.util.constants;

public enum Authority {
    READ,
    WRITE,
    DELETE,
    UPDATE,
    ADMIN, // can update, delete, write and read any object
    USER, // Can update, delete, write and read self object
}
