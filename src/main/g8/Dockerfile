# Builder docker pattern.
# First, the 'builder' uses a containerized graalvm to assemble a linux-native statically linked native binary.
# Second, the 'built' is copied the native binary and run.
#
# Step 1: Build it!
#  > docker build -t $name$ .
#
# Step 2: Run it!
#  > docker run $name$ .
#
# Step 3: Marvel in its speed and size!
#  > time docker run $name$
#  > docker images $name$

#
# BUILDER:
#
# 1. Use a dockerized native builder and 2. Let sbt's coursier know it doesn't need to install GraalVM
FROM ghcr.io/graalvm/graalvm-ce:java11-21.3.0 as binarybuilder
ENV GRAALVM_INSTALLED=true

# 1. Install native-image so we can emit a pre-built statically-linked binary and 2. Let sbt know it doesn't need to install native-image
RUN gu install native-image
ENV NATIVE_IMAGE_INSTALLED=true

# Use SBT wrapper script to alias sbt to download and use version specified within project
RUN curl -Ls https://git.io/sbt > /usr/local/bin/sbt && chmod 0755 /usr/local/bin/sbt

# Create the folders needed by the application and set the current working directory
RUN mkdir /build
WORKDIR /build

# Download (pre-fetch) sbt dependencies
COPY project/build.properties project/build.properties
RUN sbt update

# Download (pre-fetch) project dependencies
COPY build.sbt build.sbt
COPY project   project
RUN sbt update test:update runtime:update

# Build the project
COPY app/src /build/app/src

RUN sbt app/nativeImage

#
# BUILT:
#
# (note: can be 'FROM scratch' and be a few MB smaller, as the native image is compatible being a base-level docker image.  Currently running in 'alpine' for usability.)
FROM alpine:3.15.0
COPY --from=binarybuilder /build/app/target/native-image/$name$ /opt/docker/$name$
ENTRYPOINT ["/opt/docker/$name$"]
CMD []
