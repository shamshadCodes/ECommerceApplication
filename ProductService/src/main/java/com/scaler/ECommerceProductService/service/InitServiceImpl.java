package com.scaler.ECommerceProductService.service;

import com.scaler.ECommerceProductService.Repository.CategoryRepository;
import com.scaler.ECommerceProductService.Repository.OrderRepository;
import com.scaler.ECommerceProductService.Repository.PriceRepository;
import com.scaler.ECommerceProductService.Repository.ProductRepository;
import com.scaler.ECommerceProductService.controller.ProductController;
import com.scaler.ECommerceProductService.model.Category;
import com.scaler.ECommerceProductService.model.Order;
import com.scaler.ECommerceProductService.model.Price;
import com.scaler.ECommerceProductService.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InitServiceImpl implements InitService{
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private PriceRepository priceRepository;
    private OrderRepository orderRepository;

    public InitServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, PriceRepository priceRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.priceRepository = priceRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public void initialize(){
        Category electronics = new Category();
        electronics.setCategoryName("Electronics");

        electronics = categoryRepository.save(electronics);

        Price pricePhone = new Price();
        pricePhone.setPrice(50000);
        pricePhone.setCurrency("Rupees");
        pricePhone.setDiscount(10);

        Price priceLaptop = new Price();
        priceLaptop.setPrice(100000);
        priceLaptop.setDiscount(15);
        priceLaptop.setCurrency("Rupees");

        Price priceHeadphone = new Price();
        priceHeadphone.setPrice(20000);
        priceHeadphone.setDiscount(25);
        priceHeadphone.setCurrency("Rupees");

        Price priceWatch = new Price();
        priceWatch.setPrice(10000);
        priceWatch.setDiscount(0);
        priceWatch.setCurrency("Rupees");

        pricePhone = priceRepository.save(pricePhone);
        priceLaptop = priceRepository.save(priceLaptop);
        priceHeadphone = priceRepository.save(priceHeadphone);
        priceWatch = priceRepository.save(priceWatch);

        Product iPhone = new Product();
        iPhone.setName("IPhone");
        iPhone.setImage("http://www.images.com/some-image");
        iPhone.setDescription("This is an IPhone produced by Apple.");
        iPhone.setPrice(pricePhone);
        iPhone.setCategory(electronics);
        iPhone = productRepository.save(iPhone);

        Product laptop = new Product();
        laptop.setName("Macbook Air");
        laptop.setImage("http://www.images.com/some-image");
        laptop.setDescription("This is a macbook produced by Apple.");
        laptop.setPrice(priceLaptop);
        laptop.setCategory(electronics);
        laptop = productRepository.save(laptop);

        Product headphone = new Product();
        headphone.setName("Audio-Technica Model H");
        headphone.setImage("http://www.images.com/some-image");
        headphone.setDescription("This is a headphone produced by Audio-Technica.");
        headphone.setPrice(priceHeadphone);
        headphone.setCategory(electronics);
        headphone = productRepository.save(headphone);

        Product iWatch = new Product();
        iWatch.setName("IWatch");
        iWatch.setImage("http://www.images.com/some-image");
        iWatch.setDescription("This is a watch produced by Apple.");
        iWatch.setPrice(priceWatch);
        iWatch.setCategory(electronics);
        iWatch = productRepository.save(iWatch);

        Order order1 = new Order();
        order1.setProducts(List.of(iWatch, iPhone));
        order1 = orderRepository.save(order1);

        Order order2 = new Order();
        order2.setProducts(List.of(laptop, headphone));
        order2 = orderRepository.save(order2);
    }
}
