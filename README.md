# EcomHub

1. API Gateway (Port: 8080)
Purpose: Central entry point for all client requests
Routes requests to appropriate microservices
JWT Authentication - Validates tokens for protected routes
Load balancing and request filtering
Centralized security and cross-cutting concerns

Key Features:
Spring Cloud Gateway
JWT token validation
Request/Response logging
Route-based authentication


2. User Service (Port: 8081)
Purpose: Handles user management and authentication
User registration and login
JWT token generation after successful authentication
User profile management
Password encryption using BCrypt

Key Features:
User CRUD operations
JWT token creation
Secure password storage
Profile management


3. Product Service (Port: 8082)
Purpose: Manages product catalog and inventory
Product CRUD operations
Inventory management
Category-based product organization
Price and stock tracking

Key Features:
Product catalog management
Stock quantity tracking
Search and filter capabilities

