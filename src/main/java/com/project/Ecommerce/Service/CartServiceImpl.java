package com.project.Ecommerce.Service;

import java.util.List;
import java.util.stream.Stream;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.Ecommerce.Dto.CartDTO;
import com.project.Ecommerce.Dto.ProductDTO;
import com.project.Ecommerce.Entity.Cart;
import com.project.Ecommerce.Entity.CartItem;
import com.project.Ecommerce.Entity.Product;
import com.project.Ecommerce.Repository.CartItemRepository;
import com.project.Ecommerce.Repository.CartRepository;
import com.project.Ecommerce.Repository.ProductRepository;
import com.project.Ecommerce.Util.AuthUtil;
import com.project.Ecommerce.exceptions.APIException;
import com.project.Ecommerce.exceptions.ResourceNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    CartRepository cartRepository;
    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AuthUtil authUtil;

    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {

        // find existing cart or create One
        Cart cart = createCart();

        // Retreive the Product Details

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        // Perform Validation

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(
                cart.getId(),
                productId);

        if (cartItem != null) {
            throw new APIException("Product" + product.getProductName() + "already exist.");
        }
        if (product.getQuantity() == 0) {
            throw new APIException(product.getProductName() + " is not available.");
        }
        if (product.getQuantity() < quantity) {
            throw new APIException("Please,make an order of the " + product.getProductName()
                    + " less then or equal to the quantity " + product.getQuantity() + ".");
        }

        // Create cate item

        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getSpecialPrice());

        // save cart item
        cartItemRepository.save(newCartItem);

        product.setQuantity(product.getQuantity() - quantity);
        cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));

        cartRepository.save(cart);
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        List<CartItem> cartItems = cart.getCartItem();

        Stream<ProductDTO> productDTOStream = cartItems.stream().map(item -> {
            ProductDTO map = modelMapper.map(item.getProduct(), ProductDTO.class);
            map.setQuantity(item.getQuantity());
            return map;
        });
        return cartDTO;

        // return updated cart

    }

    private Cart createCart() {
        Cart userCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());
        if (userCart != null) {
            return userCart;
        }
        Cart cart = new Cart();
        cart.setTotalPrice(0.00);
        cart.setUser(authUtil.loggedInUser());
        // Cart newCart = cartRepository.save(cart);
        // return newCart;
        return cartRepository.save(cart);
    }

    @Override

    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();
        if (carts.size() == 0) {
            throw new APIException("NO cart Exist");

        }
        List<CartDTO> cartDTOs = carts.stream().map(cart -> {
            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
            cart.getCartItem().forEach(c -> c.getProduct().setQuantity(c.getQuantity()));
            List<ProductDTO> products = cart.getCartItem().stream()
                    .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class))
                    .toList();
            cartDTO.setProducts(products);
            return cartDTO;

        }).toList();
        return cartDTOs;

    }

    @Override
    public CartDTO getCart(String emailId, Long cartId) {
        Cart cart = cartRepository.findCartByEmailAndCartId(emailId, cartId);
        if (cart == null) {
            throw new ResourceNotFoundException("Cart", "cartId", cartId);

        }

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        List<ProductDTO> products = cart.getCartItem().stream()
                .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class))
                .toList();
        cartDTO.setProducts(products);
        return cartDTO;
    }

    @Transactional
    @Override
    public CartDTO updateProductQuantityInCart(Long productId, Integer quantity) {
        String emailId = authUtil.loggedInEmail();
        Cart userCart = cartRepository.findCartByEmail(emailId);
        Long cartId = userCart.getId();
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("cart", "cartId", cartId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ProductId", productId));

        if (product.getQuantity() == 0) {
            throw new APIException(product.getProductName() + " is not available.");
        }
        if (product.getQuantity() < quantity) {
            throw new APIException("Please,make an order of the " + product.getProductName()
                    + " less then or equal to the quantity " + product.getQuantity() + ".");
        }
        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);
        if (cartItem == null) {
            throw new APIException("Product " + product.getProductName() + " not exist");
        }
        int newQuantity = cartItem.getQuantity() + quantity;

        if (newQuantity < 0) {
            throw new APIException("The resulting quqntity cannot be negative.");
        }
        if (newQuantity == 0) {
            deleteProductFromCart(cartId, productId);
        } else {
            cartItem.setProductPrice(product.getSpecialPrice());
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItem.setDiscount(product.getDiscount());
            cart.setTotalPrice(cart.getTotalPrice() + (cartItem.getProductPrice() * quantity));

            cartRepository.save(cart);
        }
        CartItem updatedCartItem = cartItemRepository.save(cartItem);
        if (updatedCartItem.getQuantity() == 0) {
            cartItemRepository.deleteById(updatedCartItem.getId());
        }
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        List<CartItem> cartItems = cart.getCartItem();
        Stream<ProductDTO> productStream = cartItems.stream()
                .map(item -> {
                    ProductDTO prd = modelMapper.map(item.getProduct(), ProductDTO.class);
                    prd.setQuantity(item.getQuantity());
                    return prd;
                });
        cartDTO.setProducts(productStream.toList());
        return cartDTO;
    }
    @Transactional
    @Override
    public String deleteProductFromCart(Long cartId, Long productId) {

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));
        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);
        if (cartItem == null) {
            throw new ResourceNotFoundException("Product", "productId", productId);
        }
        cart.setTotalPrice(cart.getTotalPrice() -
                (cartItem.getProductPrice() * cartItem.getQuantity()));
        cartItemRepository.deleteCartItemByProductIdAndCartId(cartId, productId);

        return "Product " + cartItem.getProduct().getProductName() + " removed from the cart ";
    }

    @Override
    public void updateProductInCarts(Long cartId ,Long productId) {
         Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("cart", "cartId", cartId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ProductId", productId));
       CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);
       if (cartItem==null) {
        throw new APIException("Product " + product.getProductName() + " not available ");

       }
       double cartPrice = cart.getTotalPrice()-(cartItem.getProductPrice()*cartItem.getQuantity());

       cartItem.setProductPrice(product.getSpecialPrice());
       cart.setTotalPrice(cartPrice + (cartItem.getProductPrice()*cartItem.getQuantity()) ) ;

       cartItem = cartItemRepository.save(cartItem);

    }

}
