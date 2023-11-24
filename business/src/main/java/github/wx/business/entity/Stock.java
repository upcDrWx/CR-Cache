package github.wx.business.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wx
 * @description
 * @date 2023/11/13 16:57
 */
@Data
@Accessors(chain = true)
@TableName(value = "t_stock")
public class Stock {

    @TableId(value = "id")
    Long id;

    Long proId;

    Integer total;

    Integer sold;

    Integer frozen;

}
