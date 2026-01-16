use axum::body::Body;
use hello_world::{GoodbyReply, HelloReply, HelloRequest};

use tonic::{Request, Response, Status, transport::Server};
use tracing::{Level, Span, info};

pub mod hello_world {
    tonic::include_proto!("helloworld");
}

fn main() {
    println!("Hello, world!");
}

pub struct TheGreeter {}
pub mod proto {
    tonic::include_proto!("policy");

    pub(crate) const FILE_DESCRIPTOR_SET: &[u8] =
        tonic::include_file_descriptor_set!("helloworld_descriptor");
}
#[tonic::async_trait]
impl Greeter for TheGreeter {
    async fn say_hello(
        &self,
        request: Request<HelloRequest>,
    ) -> Result<Response<HelloReply>, Status> {
        match request.remote_addr() {
            Some(val) => println!("Got a request from {:?}", val),
            None => println!("Got a request from a connexion that don't have any IP address."),
        };

        let reply = hello_world::HelloReply {
            message: format!("Hello {}!", request.into_inner().name),
        };
        Ok(Response::new(reply))
    }
    async fn say_goodbye(
        &self,
        request: tonic::Request<hello_world::GoodbyRequest>,
    ) -> Result<Response<GoodbyReply>, Status> {
        println!("Got a request from {:?}", request.remote_addr());

        let reply = hello_world::GoodbyReply {
            message: format!("Goodbye {}!", request.into_inner().name),
            detail: "text".to_string(),
        };
        Ok(Response::new(reply))
    }
}
