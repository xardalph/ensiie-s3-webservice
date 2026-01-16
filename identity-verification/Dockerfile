FROM rust:1.91 AS builder
WORKDIR /usr/src/agent

# This allow to cache rust deps build.
RUN mkdir -p  src/bin && echo "fn main() {}" > src/bin/dummy.rs
# run this with docker compose from the previous folder.
COPY Cargo.toml .
RUN cargo build --release
RUN rm src/bin/dummy.rs

# Here we copy our real code and install it in the filesystem.
COPY . .
RUN cargo install --path .

# We create a small image with only the compiled binary.
FROM debian:trixie-slim
RUN apt update

# Some debug utils.
RUN apt install iputils-ping nmap -y

# Copy the executable we need in the debian small image, to avoid having all rust deps (2GB) in final image.
COPY --from=builder /usr/local/cargo/bin/identity-verification /usr/local/bin/identity-verification

ENTRYPOINT ["identity-verification"]
