package fr.frogdevelopment.assoplus.dao;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import fr.frogdevelopment.assoplus.bean.SchoolYear;

@Repository("schoolYearDao")
public class SchoolYearDaoImpl extends CommonDaoImpl<SchoolYear> implements SchoolYearDao {

    @Override
    public SchoolYear getLastShoolYear() {
        return (SchoolYear) getCriteria()
                .addOrder(Order.desc("schoolYear"))
                .setMaxResults(1)
                .uniqueResult();
    }

}
