package mc.project.online_store.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attribute {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "attribute_type_id", nullable = false)
    private AttributeType attributeType;

    @Column(nullable = false)
    private String value;
}
