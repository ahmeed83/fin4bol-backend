provider "azurerm" {
  features {}
}

# Variable definitions
variable "postgres_username" {
  type    = string
  default = "finance4bol_admin"
}

variable "postgres_password" {
  type    = string
  default = "P@ssw0rd1234"
}

variable "jwt_secret_key" {
  type    = string
  default = "jMC5hUk9qBQIQ9CKlDifBmkPOquKnfUzb5dYYAmxnpk="
}

# Resource Group
resource "azurerm_resource_group" "finance4bolRG" {
  name     = "finance4bol-resource-group"
  location = "West Europe"
}

# App Service Plan
resource "azurerm_service_plan" "finance4bolSP" {
  name                = "finance4bol-app-service-plan"
  location            = azurerm_resource_group.finance4bolRG.location
  resource_group_name = azurerm_resource_group.finance4bolRG.name
  os_type             = "Linux"
  sku_name            = "B1"
}

# App Service
resource "azurerm_linux_web_app" "finance4bolAS" {
  name                = "finance4bol-app-service"
  location            = azurerm_resource_group.finance4bolRG.location
  resource_group_name = azurerm_resource_group.finance4bolRG.name
  service_plan_id     = azurerm_service_plan.finance4bolSP.id

  site_config {
    always_on = true
    application_stack {
      #docker_image_name        = "azizcode/finance4bol:latest"
      docker_image_name        = "azizcode/finance4bol:snapshot-0.0.1-20240422T002705"
      docker_registry_url      = "https://index.docker.io"
      docker_registry_username = "azizcode"
      docker_registry_password = "vuy3cfx@gwg0HME!jqj"
    }
  }

  app_settings = {
    "WEBSITES_ENABLE_APP_SERVICE_STORAGE" = "false" # For external Docker image use
    # Define other settings as required
    "SPRING_DATASOURCE_URL"               = "jdbc:postgresql://${azurerm_postgresql_server.finance4bolPSQL.name}.postgres.database.azure.com:5432/finance4bol-db"
    "SPRING_DATASOURCE_USERNAME"          = "${var.postgres_username}@${azurerm_postgresql_server.finance4bolPSQL.name}"
    "SPRING_DATASOURCE_PASSWORD"          = var.postgres_password
    "JWT_SECRET_KEY"                      = var.jwt_secret_key
    "JWT_EXPIRATION_DAYS"                 = "1"
    "WEBSITES_PORT"                       = "80"
  }
}

# PostgresSQL Server
resource "azurerm_postgresql_server" "finance4bolPSQL" {
  name                = "finance4bol-psql-server"
  location            = azurerm_resource_group.finance4bolRG.location
  resource_group_name = azurerm_resource_group.finance4bolRG.name

  sku_name = "B_Gen5_2"

  storage_mb                   = 5120
  backup_retention_days        = 7
  geo_redundant_backup_enabled = false
  auto_grow_enabled            = true

  administrator_login          = var.postgres_username
  administrator_login_password = var.postgres_password

  version                          = "11"
  ssl_enforcement_enabled          = false
  ssl_minimal_tls_version_enforced = "TLSEnforcementDisabled"
}

# PostgresSQL Database
resource "azurerm_postgresql_database" "finance4bolPSQL_DB" {
  name                = "finance4bol-psql-db"
  resource_group_name = azurerm_resource_group.finance4bolRG.name
  server_name         = azurerm_postgresql_server.finance4bolPSQL.name
  charset             = "UTF8"
  collation           = "English_United States.1252"
}

# Allow access to PostgresSQL server from all Azure services
resource "azurerm_postgresql_firewall_rule" "allow_azure_services" {
  name                = "AllowAzureServices"
  resource_group_name = azurerm_resource_group.finance4bolRG.name
  server_name         = azurerm_postgresql_server.finance4bolPSQL.name
  start_ip_address = "0.0.0.0"  # Special case to allow access from all Azure services
  end_ip_address      = "0.0.0.0"
}

# Storage Account
resource "azurerm_storage_account" "finance4bolStorage" {
  name                     = "finance4bolsa"
  resource_group_name      = azurerm_resource_group.finance4bolRG.name
  location                 = azurerm_resource_group.finance4bolRG.location
  account_tier             = "Standard"
  account_replication_type = "LRS" # Locally-redundant storage

  static_website {
    index_document     = "index.html"
    error_404_document = "404.html"
  }
}

# Storage Blob container to hold static files
resource "azurerm_storage_container" "finance4bolStaticFiles" {
  name                  = "static-content"
  storage_account_name  = azurerm_storage_account.finance4bolStorage.name
  container_access_type = "blob"
}
