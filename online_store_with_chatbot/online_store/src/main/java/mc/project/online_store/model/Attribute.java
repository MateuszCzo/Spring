package mc.project.online_store.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Attribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "attribute_type_id", nullable = false)
    private AttributeType attributeType;

    @Column(nullable = false)
    private String value;
}
