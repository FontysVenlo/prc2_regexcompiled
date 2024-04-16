:imagesdir: images/
ifdef::env-github[]
:tip-caption: :bulb:
:note-caption: :information_source:
:important-caption: :heavy_exclamation_mark:
:caution-caption: :fire:
:warning-caption: :warning:
:imagesdir: images/
endif::[]

# prc2_regexcompiled
jmh benchmark compiling regex

This is a simple benchmark that compares between string.matches
and a compiled pattern.

To build, go to the source directory `regexcompiled`,
invoke `mvn package` and run with the `./doit` script.

image::email-regex.png[]
