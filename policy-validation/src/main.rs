use policy::validation_server::{Validation, ValidationServer};
use policy::{Claim, PolicyReturn};
use tonic::codegen::Arc;
use tonic::{Request, Response, Status, transport::Server};

pub mod policy {
    tonic::include_proto!("policy");
}

#[derive(Default)]
pub struct ValidationService {}

#[tonic::async_trait]
impl Validation for ValidationService {
    async fn validate(&self, request: Request<Claim>) -> Result<Response<PolicyReturn>, Status> {
        let claim = request.into_inner();

        // Example logic to validate the claim
        let is_valid = !claim.id.is_empty() && claim.policy_number > 0;

        let reply = PolicyReturn { r#return: is_valid };

        Ok(Response::new(reply))
    }
}

#[tokio::main]
async fn main() -> Result<(), Box<dyn std::error::Error>> {
    let addr = "0.0.0.0:50051".parse()?;
    let validation_service = ValidationService::default();

    println!("ValidationService listening on {}", addr);

    Server::builder()
        .add_service(ValidationServer::new(validation_service))
        .serve(addr)
        .await?;

    Ok(())
}
