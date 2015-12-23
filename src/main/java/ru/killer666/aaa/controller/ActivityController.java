package ru.killer666.aaa.controller;

import com.google.common.base.Preconditions;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.killer666.aaa.service.GsonService;
import ru.killer666.trpo.aaa.domains.Accounting;
import ru.killer666.trpo.aaa.domains.AccountingResource;
import ru.killer666.trpo.aaa.domains.ResourceWithRole;
import ru.killer666.trpo.aaa.services.HibernateSessionService;

@Controller
@RequestMapping(value = "/ajax/activity")
public class ActivityController {
    @Autowired
    GsonService gsonService;

    @Autowired
    HibernateSessionService sessionService;

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getAll(@RequestParam(value = "id", required = false) String id,
                         @RequestParam(value = "authorityId", required = false) String authorityId) throws Exception {
        Session session = this.sessionService.getObject().openSession();
        Object toSerialize;

        if (id != null) {
            toSerialize = session.get(Accounting.class, Integer.parseInt(id));
        } else if (authorityId != null) {
            ResourceWithRole resourceWithRole = (ResourceWithRole) session.get(ResourceWithRole.class, Integer.parseInt(authorityId));

            Preconditions.checkArgument(resourceWithRole != null, "authority not found");

            Criteria criteria = session.createCriteria(AccountingResource.class);
            criteria.add(Expression.eq("resourceWithRole", resourceWithRole));

            toSerialize = criteria.list();
        } else {
            toSerialize = session.createCriteria(Accounting.class).list();
        }

        session.close();

        return this.gsonService.getObject().toJson(toSerialize);
    }
}

