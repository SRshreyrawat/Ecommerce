package com.project.Ecommerce.Entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name="payment")
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long paymentId;
    @OneToOne(mappedBy = "payment",cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private Order order;

    @NotBlank
    @Size(min=4,message="Payement method must contain atleast 4 character")
    private String paymentMethod;


    public Payment(Long paymentId, String pgPaymentId, String pgStatus, String pgReponseMessage, String pgName) {
        this.paymentId = paymentId;
        this.pgPaymentId = pgPaymentId;
        this.pgStatus = pgStatus;
        this.pgReponseMessage = pgReponseMessage;
        this.pgName = pgName;
    }
    // pg ==> paymentGateWay
    private String pgPaymentId;
    private String pgStatus;
    private String pgReponseMessage;
    private String pgName;


}
