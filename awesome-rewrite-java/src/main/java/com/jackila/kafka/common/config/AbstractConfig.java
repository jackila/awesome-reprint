package com.jackila.kafka.common.config;


import com.jackila.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Author: jackila
 * @Date: 17:13 2020-09-01
 */
public class AbstractConfig {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final Set<String> used;
    private final Map<String,?> originals;

    private final Map<String,Object> values;

    public AbstractConfig(ConfigDef definition,Map<?,?> originals,boolean doLog) {

        for(Object key: originals.keySet()){
            if(!(key instanceof String)){
                throw  new ConfigException(key.toString(),originals.get(key),"key must be a string.");
            }
        }
        this.originals = (Map<String, ?>) originals;
        this.values = definition.parse(this.originals);
        this.used = Collections.synchronizedSet(new HashSet<String>());
        if(doLog){
            logAll();
        }
    }

    private  void logAll(){
        StringBuilder b = new StringBuilder();
        b.append(getClass().getSimpleName());
        b.append(" values: ");
        b.append(Utils.NL);
        for (Map.Entry<String, Object> entry : this.values.entrySet()) {
            b.append('\t');
            b.append(entry.getKey());
            b.append(" = ");
            b.append(entry.getValue());
            b.append(Utils.NL);
        }
        log.info(b.toString());
    }
}
