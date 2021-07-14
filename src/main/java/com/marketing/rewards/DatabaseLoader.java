package com.marketing.rewards;

import com.marketing.rewards.domain.Customer;
import com.marketing.rewards.domain.Product;
import com.marketing.rewards.domain.Purchase;
import com.marketing.rewards.repository.CustomerRepository;
import com.marketing.rewards.repository.ProductRepository;
import com.marketing.rewards.repository.PurchaseRepository;
import com.marketing.rewards.repository.RewardRepository;
import com.marketing.rewards.util.Utility;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Random;

@Component
public class DatabaseLoader implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final PurchaseRepository purchaseRepository;
    private final RewardRepository rewardRepository;

    @Autowired
    public DatabaseLoader(CustomerRepository customerRepository,
                          ProductRepository productRepository,
                          PurchaseRepository purchaseRepository,
                          RewardRepository rewardRepository){
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.purchaseRepository = purchaseRepository;
        this.rewardRepository = rewardRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        // create 6 customers with random purchases
        this.customerRepository.save(getCustomer());
        this.customerRepository.save(getCustomer());
        this.customerRepository.save(getCustomer());
        this.customerRepository.save(getCustomer());
        this.customerRepository.save(getCustomer());
        this.customerRepository.save(getCustomer());
    }

    /**
     * Create Purchases with Products having random prices
     * The price is a double value with scale set to 2 and a rounding mode {@link RoundingMode#HALF_UP}
     * Create Customer making these purchases
     * @return Customer having a purchase made
     */
    private Customer getCustomer() {
        Random random = new Random();
        Purchase purchase = new Purchase();

        // create a Product with a random Price value less than $150.0
        Product product = new Product();
        BigDecimal price = BigDecimal.valueOf(random.nextInt(149) + random.nextDouble());
        product.setPrice(price.setScale(2, RoundingMode.HALF_UP).doubleValue());
        productRepository.save(product);

        // create a Customer with a random first-name and last-name
        String firstName = RandomStringUtils.random(5, true, false);
        String lastName = RandomStringUtils.random(5, true, false);
        Customer customer = new Customer(firstName, lastName);
        customerRepository.save(customer);

        // create a Purchase of a Product made by a Customer on a certain date
        purchase.setProducts(Arrays.asList(product));
        purchase.setCustomer(customer);
        Instant instant = Utility.getRandomDateInPastThreeMonths();
        ZoneId z = ZoneId.of( "America/New_York" );
        purchase.setPurchaseDate(LocalDate.ofInstant( instant , z ));
        purchaseRepository.save(purchase);

        customer.setPurchaseList(Arrays.asList(purchase));
        return customer;
    }


}
