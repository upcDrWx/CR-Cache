package github.wx.business.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author wx
 * @description
 * @date 2023/11/13 16:54
 */
@Data
@Accessors(chain = true)
@TableName(value = "t_order")
public class Order implements Serializable {

    @TableId(value = "id")
    Long id;

    String orderNumber;

    Double money;

    Integer status;
}
