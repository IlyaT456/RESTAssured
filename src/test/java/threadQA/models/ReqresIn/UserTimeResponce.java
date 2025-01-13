package threadQA.models.ReqresIn;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTimeResponce extends UserTime {

    private String updatedAt;
}
