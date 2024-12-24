package threadQA.models.ReqresIn;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ColorsData {

    public Integer id;
    public String name;
    public Integer year;
    public String color;
    public String pantone_value;
}
