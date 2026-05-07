package com.ecommerce.product.repository;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long>{

    Optional<Product> findByName(String name);
    Optional<Product> existByname(String name);
    
}
