package ru.killer666.aaa.service;

import com.google.common.base.Preconditions;
import ru.killer666.aaa.RoleEnum;
import ru.killer666.trpo.aaa.services.RoleResolverService;

import java.util.EnumSet;
import java.util.Iterator;

public class RoleResolverServiceImpl implements RoleResolverService {
    @Override
    public Enum<?> resolve(int ordinal) {
        EnumSet<RoleEnum> set = EnumSet.allOf(RoleEnum.class);

        if (ordinal < set.size()) {
            Iterator iter = set.iterator();

            for (int i = 0; i < ordinal; i++) {
                iter.next();
            }

            Enum<?> rval = (Enum<?>) iter.next();

            Preconditions.checkArgument(rval.ordinal() == ordinal);

            return rval;
        }

        throw new IllegalArgumentException("Invalid value " + ordinal + " for " + RoleEnum.class.getName() + ", must be < " + set.size());
    }
}
