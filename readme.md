# Logger

## Introduction

This doc contains information on how to use the Logger library. This library enables you to log whatever you want easy and fast using the YOCTU Logger RestAPI.

### Default values

Although you may want to change every parameter, we put some defaults in place if you don't.
Both the _level_ and _category_ parameters will only hold previously specified default values which mustn't be changed:

__Level__
LVL_DEBUG
LVL_INFO
LVL_WARNING
LVL_ERROR
LVL_PANIC

__Category__
CAT_SECURITY
CAT_PERFORMANCE
CAT_BUSINESS
CAT_AUDIT
CAT_SQL
CAT_TECHNICAL
CAT_TRACKING

The following table contains all the changeable parameters with their type and default value:

| NAME      | TYPE     | DEFAULT         |
|-----------|:--------:|:---------------:|
| url       | String   | "url"           |
| level     | Int      | LVL_INFO        |
| env       | String   | ""              |
| hostname  | String   | "" 			 |
| namespace | String   | ""              |
| origin    | String   | ""    	         |
| binary    | String   | ""              |
| user      | String   | ""				 |
| category  | Int      | CAT_TECHNICAL   |
| message   | String   | "message"       |
| context   | String[] | {}              |
| auth      | String   | "logger"        |

You have the ability to change these values at any point using the following methodes:
```java
myLogger.getUrl();
```
```java
myLogger.setUrl("url");
```

## Usage

### Getting started

To set up the Logger you have to create an object out of the class Logger whilst providing a url as parameter.
**auth** _is used for authentication -> API key_
Leave the second parameter as empty string if you don't have/use an 

```java
Logger myLogger = new Logger("url", "auth");
```

Although this is recommended you are also able to change it later with:

```java
myLogger.setUrl("New URL");
```

> _Note that the URL has to be a String_

### Create new entry

To create a new entry you have to call the following method and provide all needed parameters:

__input__
int level, int category, String message, String context[], String env, String hostname, String namespace, String origin, String binary, String user

_Note that level and category have to be set using the default values as shown below_

```java
String response = myLogger.postLog(Logger.LVL_WARNING, Logger.CAT_AUDIT, "message", {array}, "env", "hostname", "namespace", "http", "binary", "user");
```
__output__
The output is always a String. It contains either the requested information or an error message.

### Get multiple logs

To get multiple logs you only have to call the following method with two parameters. The first one specifies how many logs you want to receive, the second one specifies which page should be send.

```java
myLogger.getLogs(5,1);
```
__output__
The output is always a String. It contains either the requested information or an error message.

### Get specific log

To get a specific log you have to know the __ID__ of the log in question.

Simply call the function with the ID as __Int__ parameter:

```java
myLogger.getLogById(123456789);
```
__output__
The output is always a String. It contains either the requested information or an error message.

### Delete specific log

In case you want to delete a specific log you have to know its __ID__. Be careful as to not delete an important log by accident!

Simply call the function with the ID as __Int__ parameter;

```java
myLogger.deleteLog(123456789);
```
__output__
The output is always a String. It contains either the requested information or an error message.# logger
