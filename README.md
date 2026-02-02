apache-shiro-poc

Goal
- Minimal PoC demonstrating Apache Shiro authentication (login/logout) and authorization (role/permission checks) using an INI realm.

Prerequisites
- Java 17+ (works with Java 11+ as well)
- Maven 3.8+

Run
1) mvn -q test

What it does
- Loads Shiro configuration from src/test/resources/shiro.ini
- Logs in a user with username/password
- Prints whether the subject is authenticated
- Checks a role and a permission
- Shows a failing login scenario

Customize
- Edit users/roles/permissions in src/test/resources/shiro.ini
- Edit test cases in src/test/java/poc/ShiroPocTest.java
