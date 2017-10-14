[![APACHE v2 License](https://img.shields.io/badge/license-apachev2-blue.svg?style=flat)](LICENSE-2.0.txt) [![Latest Release](https://img.shields.io/maven-central/v/org.simplejavamail/simple-java-mail.svg?style=flat)](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.simplejavamail%22%20AND%20a%3A%22simple-java-mail%22) [![Javadocs](http://www.javadoc.io/badge/org.simplejavamail/simple-java-mail.svg?color=brightgreen)](http://www.javadoc.io/doc/org.simplejavamail/simple-java-mail) [![Build Status](https://img.shields.io/travis/bbottema/simple-java-mail.svg?style=flat)](https://travis-ci.org/bbottema/simple-java-mail) [![Codacy](https://img.shields.io/codacy/9f142ca8c8c640c984835a8ae02d29f3.svg?style=flat)](https://www.codacy.com/app/b-bottema/simple-java-mail)

# Simple Java Mail #

Simple Java Mail is the simplest to use lightweight mailing library for Java, while being able to send complex emails including **authenticated socks proxy**(!), **attachments**, **embedded images**, **custom headers and properties**, **robust address validation**, **build pattern** and even **DKIM signing** and **external configuration files** with **property overriding**, **Spring support** and **Email conversion** tools. Just send your emails without dealing with RFC's.

The Simple Java Mail library is a thin layer on top of the JavaMail smtp mailing API that allows users to define emails on a high abstraction level without having to deal with mumbo jumbo such a 'multipart' and 'mimemessage'.

### [simplejavamail.org](http://www.simplejavamail.org) ###

```java
ConfigLoader.loadProperties("simplejavamail.properties"); // optional default
ConfigLoader.loadProperties("overrides.properties"); // optional extra

Email email = new Email();

email.setFromAddress("lollypop", "lolly.pop@mymail.com");
email.setReplyToAddress("lollypop", "lolly.pop@othermail.com");
email.addNamedToRecipients("lollypop", "lolly.pop@somemail.com");
email.addNamedToRecipients("C. Cane", "candycane@candyshop.org");
email.addRecipients("optional default name", CC, "rocky@candyshop.org; Chocobo <chocobo@candyshop.org>");
email.setSubject("hey");
email.setText("We should meet up! ;)");
email.setTextHTML("&lt;img src=&#39;cid:wink1&#39;&gt;&lt;b&gt;We should meet up!&lt;/b&gt;&lt;img src=&#39;cid:wink2&#39;&gt;");
email.addEmbeddedImage("wink1", imageByteArray, "image/png");
email.addEmbeddedImage("wink2", imageDatesource);
email.addAttachment("invitation", pdfByteArray, "application/pdf");
email.addAttachment("dresscode", odfDatasource);

email.addHeader("X-Priority", 5);
email.setUseReturnReceiptTo(true);

email.signWithDomainKey(privateKeyData, "somemail.com", "selector");

new Mailer(
    new ServerConfig("smtp.host.com", 587, "user@host.com", "password"),
    TransportStrategy.SMTP_TLS,
    new ProxyConfig("socksproxy.host.com", 1080, "proxy user", "proxy password")
).sendMail(email);
```

---


Simple Java Mail is available in Maven Central:

```xml
<dependency>
    <groupId>org.simplejavamail</groupId>
    <artifactId>simple-java-mail</artifactId>
    <version>4.4.5</version>
</dependency>
```

### Latest Progress ###

[v4.4.5](http://search.maven.org/#artifactdetails%7Corg.simplejavamail%7Csimple-java-mail%7C4.4.5%7Cjar) (2-September-2017)

- [#101](https://github.com/bbottema/simple-java-mail/issues/101) API backwards compatibility update, reinstate old addRecipient API as deprecated 
(sorry for removing it abruptly)


[v4.4.4](http://search.maven.org/#artifactdetails%7Corg.simplejavamail%7Csimple-java-mail%7C4.4.4%7Cjar) (23-August-2017)

API usability release. **This relase streamlined the recipient setters, breaking backwards compatibility (but straightforward to fix)**

- [#95](https://github.com/bbottema/simple-java-mail/issues/95) Feature: Add support native API for setting Return-Receipt-To header
- [#93](https://github.com/bbottema/simple-java-mail/issues/93) Feature: Add support native API for setting Disposition-Notification-To header
- [#91](https://github.com/bbottema/simple-java-mail/issues/91) **Feature: Add support for parsing preformatted email addresses that include both 
name and address**
- [#94](https://github.com/bbottema/simple-java-mail/issues/94) Bugfix: A single EmailBuilder would build emails that all share the same collections
 for recipients, attachments and embedded images
- [#98](https://github.com/bbottema/simple-java-mail/issues/98) Bugfix: Subject and body content should be optional


[v4.3.0](http://search.maven.org/#artifactdetails%7Corg.simplejavamail%7Csimple-java-mail%7C4.3.0%7Cjar) (12-August-2017)

Security and timeout release. 

This version safeguards against SMTP injection attack from external values entering the library through *Email* instance. Also, this release 
introduces default/configurable timeouts for connecting, reading and writing when sending an email.

- [#89](https://github.com/bbottema/simple-java-mail/issues/89) Support multiple delimited recipient addresses sharing the same TO/CC/BCC name
- [#88](https://github.com/bbottema/simple-java-mail/issues/88) **Safeguard subject property (and others) against SMTP CRLF injection attacks**
- [#85](https://github.com/bbottema/simple-java-mail/issues/85) **Apply configurable timeouts when sending emails**
- [#83](https://github.com/bbottema/simple-java-mail/issues/83) Parse INLINE attachments without ID as regular attachments when converting (mostly 
applicable to Apple emails)


[v4.2.3](http://search.maven.org/#artifactdetails%7Corg.simplejavamail%7Csimple-java-mail%7C4.2.3%7Cjar) (21-May-2017)

- [#79](https://github.com/bbottema/simple-java-mail/issues/79): Enhancement: define custom message ID on the Email object
- [#74](https://github.com/bbottema/simple-java-mail/issues/74): v4.2.3-java-6-release: A java6 version with limited capabilities:
    I've released a customised java6 release with a customised outlook-message-parser 1.1.16-java6-release. **This is the last java6 release** I 
    will do,
     as it is simply too much manual labor to create a limited second edition.

    For this edition, I've removed the JDK7 Phaser completely which has the following consequences:

    - If authenticated proxy is used, the bridging proxy server will not be shut down automatically (and might not run the second time)
    - If mails are sent in async mode, the connection pool will not be shut down anymore by itself
    
    This means your server/application might not stop properly due to lingering processes. To be completely safe, only send emails in sync mode (used by default) and don't use authenticated proxy config.


[v4.2.2](http://search.maven.org/#artifactdetails%7Corg.simplejavamail%7Csimple-java-mail%7C4.2.2%7Cjar) (10-May-2017)

- [#73](https://github.com/bbottema/simple-java-mail/issues/73): Patch: fix for sending emails in async mode, which makes sure the connection pool is 
always closed when the last *known* email has been sent. Without this fix, the connection pool keeps any parent process running (main thread or 
Tomcat for example) until a hard kill


[v4.2.1](http://search.maven.org/#artifactdetails%7Corg.simplejavamail%7Csimple-java-mail%7C4.2.1%7Cjar) (12-Feb-2017)

Patch: streamlined convenience methods for adding recipients.


[v4.2.0](http://search.maven.org/#artifactdetails%7Corg.simplejavamail%7Csimple-java-mail%7C4.2.0%7Cjar) (12-Feb-2017)

**Major feature: Using the EmailConverter you can now convert between Outlook .msg, EML, MimeMessage and Email**!

- [#66](https://github.com/bbottema/simple-java-mail/issues/66): Feature: convert email to EML
- [#65](https://github.com/bbottema/simple-java-mail/issues/65): Feature: read outlook messages from .msg file
- [#64](https://github.com/bbottema/simple-java-mail/issues/64): **Feature: Added support for logging-only mode that skips the actual sending of emails**
- [#63](https://github.com/bbottema/simple-java-mail/issues/63): Feature: Already including in previous patch update: Spring support (read properties from Spring context)
- [#69](https://github.com/bbottema/simple-java-mail/issues/69): Enhancement: Expanded EmailBuilder API to inlude more options for setting (multiple) recipients
- [#70](https://github.com/bbottema/simple-java-mail/issues/70): Enhancement: Most public API now have defensive null-checks for required fields (Fail Fast support)
- [#68](https://github.com/bbottema/simple-java-mail/issues/68): Bugfix: Name should be required for embedded images (added safeguards)
- [#67](https://github.com/bbottema/simple-java-mail/issues/67): Bugfix: Error when name was omitted for attachment
- minor: added methods on AttachmentResource that reads back the content as (encoded) String
- other: internal testing is now done using Wiser SMTP test server for testing live sending emails

**Note**: Starting this release, there will always be a Java6 compatible release as well versioned: "x.y.z-java6-release"


[v4.1.3](http://search.maven.org/#artifactdetails%7Corg.simplejavamail%7Csimple-java-mail%7C4.1.3%7Cjar) (28-Jan-2017)

- [#61](https://github.com/bbottema/simple-java-mail/issues/61): Feature: Add support for providing your own Properties object
- [#63](https://github.com/bbottema/simple-java-mail/issues/63): **Feature: Spring support (read properties from Spring context)**
- [#58](https://github.com/bbottema/simple-java-mail/issues/58): Bugfix: Add support for non-English attachment and embedded image names
- [#62](https://github.com/bbottema/simple-java-mail/issues/62): Bugfix: Empty properties loaded from config should be considered null

**NOTE**: ConfigLoader moved from `/internal/util` to `/util`


[v4.1.2](http://search.maven.org/#artifactdetails%7Corg.simplejavamail%7Csimple-java-mail%7C4.1.2%7Cjar) (07-Nov-2016)

- [#52](https://github.com/bbottema/simple-java-mail/issues/52): bug fix for windows / linux disparity when checking socket status
- [#56](https://github.com/bbottema/simple-java-mail/issues/56): bug fix for IOException when signing dkim with a File reference


[v4.1.1](http://search.maven.org/#artifactdetails%7Corg.simplejavamail%7Csimple-java-mail%7C4.1.1%7Cjar) (30-Jul-2016)

- [#50](https://github.com/bbottema/simple-java-mail/issues/50): bug fix for manual naming datasources


[v4.1.0](http://search.maven.org/#artifactdetails%7Corg.simplejavamail%7Csimple-java-mail%7C4.1.0%7Cjar) (22-Jul-2016)

- [#48](https://github.com/bbottema/simple-java-mail/issues/48): Added programmatic support trusting hosts for SSL connections
- [#47](https://github.com/bbottema/simple-java-mail/issues/47): Honor given names, deduce extension from datasource name, and more robust support for parsing mimemessages


[v4.0.0](http://search.maven.org/#artifactdetails%7Corg.simplejavamail%7Csimple-java-mail%7C4.0.0%7Cjar) (05-Jul-2016)

- [#41](https://github.com/bbottema/simple-java-mail/issues/41): added support for fast parallel batch processing
- [#42](https://github.com/bbottema/simple-java-mail/issues/42): **added support for config files**
- [#43](https://github.com/bbottema/simple-java-mail/issues/43): removed logging implementation dependencies from distribution and documented various sample configs
- [#39](https://github.com/bbottema/simple-java-mail/issues/39): simplified and renamed packages to reflect the domain name of the new website: [simplejavamail.org](http://www.simplejavamail.org)
- [#38](https://github.com/bbottema/simple-java-mail/issues/38): added support for anonymous proxy
- [#38](https://github.com/bbottema/simple-java-mail/issues/38): **added support for authenticated proxy** 

**NOTE**: All packages have been renamed to "org.simplejavamail.(..)"
**NOTE**: Switched to Java 7


[v3.1.1](http://search.maven.org/#artifactdetails%7Corg.codemonkey.simplejavamail%7Csimple-java-mail%7C3.1.1%7Cjar) (11-May-2016)

**Major feature: DKIM support**!

- [#36](https://github.com/bbottema/simple-java-mail/issues/36): Added proper toString and equals methods for the Email classes
- [#33](https://github.com/bbottema/simple-java-mail/issues/33): Added support for DKIM domain key signing

*NOTE*: this is the last release still using Java 6. Next release will be using Java 7.
/edit: starting with 4.2.0 every release will now have a "x.y.z-java6-release" release as well


[v3.0.2](http://search.maven.org/#artifactdetails%7Corg.codemonkey.simplejavamail%7Csimple-java-mail%7C3.0.2%7Cjar) (07-May-2016)

- [#35](https://github.com/bbottema/simple-java-mail/issues/35): added proper .equals() and .toString() methods
- [#34](https://github.com/bbottema/simple-java-mail/issues/34): Fixed bug when disposition is missing (assume it is an attachment)
- other: added findbugs support internally


[v3.0.1](http://search.maven.org/#artifactdetails%7Corg.codemonkey.simplejavamail%7Csimple-java-mail%7C3.0.1%7Cjar) (29-Feb-2016)

  * [#31](https://github.com/bbottema/simple-java-mail/issues/31): Fixed EmailAddressCriteria.DEFAULT and clarified Javadoc


[v3.0.0](http://search.maven.org/#artifactdetails%7Corg.codemonkey.simplejavamail%7Csimple-java-mail%7C3.0.0%7Cjar) (26-Feb-2016)

  * [#30](https://github.com/bbottema/simple-java-mail/issues/30): Improved the demonstration class to include attachments and embedded images
  * [#29](https://github.com/bbottema/simple-java-mail/issues/29): The package has been restructured for future maintenance, breaking backwards compatibility
  * [#28](https://github.com/bbottema/simple-java-mail/issues/28): Re-added improved email validation facility
  * [#22](https://github.com/bbottema/simple-java-mail/issues/22): Added conversion to and from MimeMessage. You can now consume and produce MimeMessage objects with simple-java-mail

  
[v2.5.1](http://search.maven.org/#artifactdetails%7Corg.codemonkey.simplejavamail%7Csimple-java-mail%7C2.5.1%7Cjar) (19-Jan-2016)

  * [#25](https://github.com/bbottema/simple-java-mail/issues/25): Added finally clause that will always close socket properly in case of an exception

  
[v2.5](http://search.maven.org/#artifactdetails%7Corg.codemonkey.simplejavamail%7Csimple-java-mail%7C2.5%7Cjar) (19-Jan-2016)

  * [#24](https://github.com/bbottema/simple-java-mail/issues/24): Updated dependencies SLF4J to 1.7.13 and switched to the updated javax mail package com.sun.mail:javax.mail 1.5.5

  
[v2.4](http://search.maven.org/#artifactdetails%7Corg.codemonkey.simplejavamail%7Csimple-java-mail%7C2.4%7Cjar) (12-Aug-2015)

  * [#21](https://github.com/bbottema/simple-java-mail/issues/21): builder API uses CC and BCC recipient types incorrectly


[v2.3](http://search.maven.org/#artifactdetails%7Corg.codemonkey.simplejavamail%7Csimple-java-mail%7C2.3%7Cjar) (21-Jul-2015)

  * [#19](https://github.com/bbottema/simple-java-mail/issues/19): supporting custom Session Properties now and emergency access to internal Session object.


[v2.2](http://search.maven.org/#artifactdetails%7Corg.codemonkey.simplejavamail%7Csimple-java-mail%7C2.2%7Cjar) (09-May-2015)

  * [#3](https://github.com/bbottema/simple-java-mail/issues/3): turned off email regex validation by default, with the option to turn it back on
  * [#7](https://github.com/bbottema/simple-java-mail/issues/7): fixed NullPointerException when using your own Session instance
  * [#10](https://github.com/bbottema/simple-java-mail/issues/10): properly UTF-8 encode recipient addresses
  * [#14](https://github.com/bbottema/simple-java-mail/issues/14): switched to SLF4J, so you can easily use your own selected logging framework
  * [#17](https://github.com/bbottema/simple-java-mail/issues/17): Added [fluent interface](http://en.wikipedia.org/wiki/Builder_pattern) for building emails (see [manual](https://github.com/bbottema/simple-java-mail/wiki/Manual) for an example)


[v2.1](http://search.maven.org/#artifactdetails%7Corg.codemonkey.simplejavamail%7Csimple-java-mail%7C2.1%7Cjar) (09-Aug-2012)

  * fixed character encoding for reply-to, from, to, body text and headers (to UTF-8)
  * fixed bug where Recipient was not public resulting in uncompilable code when calling email.getRecipients()


[v2.0](http://search.maven.org/#artifactdetails%7Corg.codemonkey.simplejavamail%7Csimple-java-mail%7C2.0%7Cjar) (20-Aug-2011)

  * added support for adding open headers, such as 'X-Priority: 2'


[v1.9.1](http://search.maven.org/#artifactdetails%7Corg.codemonkey.simplejavamail%7Csimple-java-mail%7C1.9.1%7Cjar) (08-Aug-2011)

  * updated for Maven support


v1.9 (6-Aug-2011)

  * added support for JavaMail's reply-to address
  * made port optional as to support port defaulting based on protocol
  * added transport strategy default in the createSession method
  * tightened up thrown exceptions (MailException instead of RuntimeException)
  * added and fixed [JavaDoc](http://simple-java-mail.googlecode.com/svn/trunk/javadoc/users/index.html)


v1.8

  * Added support for TLS (tested with gmail)


v1.7 (22-Mar-2011)

Added support for SSL! (tested with gmail)

  * improved argument validation when creating a Mailer without preconfigured Session instance

known possible issue: SSL self-signed certificates might not work (yet). Please let me know by e-mail or create a new issue


v1.6

Completed migration to Java Simple Mail project.

  * removed all Vesijama references
  * updated TestMail demonstration class for clarification
  * updated readme.txt for test run instructions
  * included log4j.properties


v1.4 (15-Jan-2011)


vX.X (26-Apr-2009)

  * Initial upload to Google Code.
