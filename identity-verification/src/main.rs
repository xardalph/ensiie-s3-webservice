use axum::{Json, Router, extract, routing::post};
use http::StatusCode;
use serde::{Deserialize, Serialize};
use tokio::signal;
use tracing_subscriber::{EnvFilter, layer::SubscriberExt, util::SubscriberInitExt};

#[tokio::main]
async fn main() -> Result<(), Box<dyn std::error::Error>> {
    println!("starting program...");

    tracing_subscriber::registry()
        .with(EnvFilter::new(
            std::env::var("RUST_LOG").unwrap_or_else(|_| "debug".into()),
        ))
        .with(tracing_subscriber::fmt::layer())
        .try_init()
        .unwrap();

    let app = Router::new().route("/claim/validate", post(check_user));

    let listener = tokio::net::TcpListener::bind("0.0.0.0:80").await.unwrap();
    println!("listenning to 0.0.0.0:80...");
    // Ensure we use a shutdown signal to abort the œion task.
    axum::serve(listener, app.into_make_service())
        .with_graceful_shutdown(shutdown_signal())
        .await;
    return Ok(());
}
#[derive(Clone, Serialize, Deserialize, Debug)]
pub struct Claim {
    pub id: i64,
    pub name: String,
    pub surname: String,
    #[serde(rename = "phoneNumber")]
    pub phone_number: String,
    pub address: String,
    #[serde(rename = "claimType")]
    pub claim_type: i64,
    #[serde(rename = "policyNumber")]
    pub policy_number: i64,
    pub description: String,
}
pub async fn check_user(
    extract::Json(claim): extract::Json<Claim>,
) -> Result<(http::StatusCode, axum::Json<bool>), ()> {
    println!("Received claim: {:?}", claim);
    return Ok((StatusCode::OK, Json(true)));
}

pub async fn shutdown_signal() {
    let ctrl_c = async {
        signal::ctrl_c()
            .await
            .expect("failed to install Ctrl+C handler");
    };

    #[cfg(unix)]
    let terminate = async {
        signal::unix::signal(signal::unix::SignalKind::terminate())
            .expect("failed to install signal handler")
            .recv()
            .await;
    };

    #[cfg(not(unix))]
    let terminate = std::future::pending::<()>();

    tokio::select! {
        _ = ctrl_c => { },
        _ = terminate => { },
    }
}
