package com.pccw.backend.util;

import com.pccw.backend.annotation.JsonResultParamMapAnnotation;
import com.pccw.backend.annotation.JsonResultParamMapPro;
import com.pccw.backend.bean.GeneralBean;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.loader.plan.build.internal.spaces.QuerySpaceHelper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.JoinColumn;
import javax.persistence.criteria.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;


import com.pccw.backend.annotation.PredicateAnnotation;
import com.pccw.backend.annotation.PredicateType;


/**
 * LastUpdatedBy: KEN,小明,wtw
 * LastUpdatedAt: 5/12/2019
 * Desc: 转换工具类
 */

@Aspect
@Slf4j
@Component
 public class Convertor {

	/**
	 * for multiple condition search
	 */
    public static <T> Specification<T> convertSpecification(Object o)
    // public static <T> Specification<T> convertSpecification(Class<? extends BaseSearchBean> cls,Object o)
			throws IllegalArgumentException, IllegalAccessException {

	    // get all Fields which haved the PredicateAnnotation uysing reflact
		Field[] fieldArr = o.getClass().getDeclaredFields();

		ArrayList<Parm> arr = new ArrayList<Parm>();

		for (Field f : fieldArr) {
			f.setAccessible(true);
			Object obj = f.get(o);
			if(obj!=null&&!obj.toString().equals("")){
				PredicateAnnotation annotation = f.getAnnotation(PredicateAnnotation.class);
				if(annotation!=null){
					Parm parm = new Parm(annotation.type(), f.getName(), obj);
					arr.add(parm);
					log.info(arr.toString());
				}
			}
		}

		// return multiple search condition
        return new Specification<T>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query,
			CriteriaBuilder builder) {
				// builder.createQuery()
				List<Predicate> list = new ArrayList<>();

				arr.stream().forEach( a->{
					Parm parm = (Parm)a;

					PredicateType code = PredicateType.getByValue(parm.getPredicateType().getCode());

					switch (code) {
						case EQUEL:
//							list.add(builder.equal(root.get(parm.getName()).as(String.class),parm.getValue()));
							list.add(builder.equal(builder.lower(root.get(parm.getName()).as(String.class)),parm.getValue().toString().toLowerCase()));
						break;
						case LIKE:
//							list.add(builder.like(root.get(parm.getName()).as(String.class),"%"+parm.getValue()+"%"));
							list.add(builder.like(builder.lower(root.get(parm.getName()).as(String.class)),"%"+parm.getValue().toString().toLowerCase()+"%"));
						break;
						case LESS_THAN:
							list.add(builder.lessThan(root.get(parm.getName()).as(String.class),parm.getValue().toString()));
						break;
						case LESSTHAN_OR_EQUEL:
							list.add(builder.lessThanOrEqualTo(root.get(parm.getName()).as(String.class),parm.getValue().toString()));
						break;
						case GREATER_THAN:
						list.add(builder.greaterThan(root.get(parm.getName()).as(String.class),parm.getValue().toString()));
						break;
						case GREATERTHAN_OR_EQUEL:
							list.add(builder.greaterThanOrEqualTo(root.get(parm.getName()).as(String.class),parm.getValue().toString()));
						break;
                         case BETWEEN:
                         String[] arr = (String[])parm.getValue();
							 list.add(builder.between(root.get(parm.getName()).as(String.class),arr[0],arr[1]));
                         break;
                         case IN:
							List<String> inArr = (List<String>)parm.getValue();
							CriteriaBuilder.In<String> in = builder.in(root.get(parm.getName()).as(String.class));
							for (Object s : inArr) {
								in.value((String) s);
							}
							list.add(in);
						 break;
						default:
							break;
					}
				});
				return builder.and(list.stream().toArray(Predicate[]::new));
			}
		};
	}

	/**将map值全部转换为小写
	 *
	 * @param orgMap
	 * @return
	 */
	public static Map<String, Object> transformLowerCase(Map<String, Object> orgMap) {
		Map<String, Object> resultMap = new HashMap<>();
		if (orgMap == null || orgMap.isEmpty()) {
			return resultMap;
		}
		Set<String> keySet = orgMap.keySet();
		for (String key : keySet) {
			String newKey = key.toLowerCase();
			resultMap.put(newKey, orgMap.get(key));
		}
		return resultMap;
	}

	/**
	 * 将Map中的key由下划线转换为驼峰
	 * @param map
	 * @return
	 */
	public static Map<String, Object> formatHumpName(Map<String, Object> map) {
		Map<String, Object> newMap = new HashMap<String, Object>();
		Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> entry = it.next();
			String key = entry.getKey();
			String newKey = toFormatHump(key);
			newMap.put(newKey, entry.getValue());
		}
		return newMap;
	}

	/**
	 * 将字符串转由下划线转换为驼峰
	 * @param colName
	 * @return
	 */
	public static String toFormatHump(String colName) {
		StringBuffer sb = new StringBuffer();
		String[] str = colName.toLowerCase().split("_");
		int i = 0;
		for (String s : str) {
			if (s.length() == 1) {
				s = s.toUpperCase();
			}
			i++;
			if (i == 1) {
				sb.append(s);
				continue;
			}
			if (s.length() > 0) {
				sb.append(s.substring(0, 1).toUpperCase());
				sb.append(s.substring(1));
			}
		}
		return sb.toString();
	}

	public static Date beginOfDay(Date date) {
		if(Objects.isNull(date)) return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY,0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		Date beginDate = cal.getTime();
		return beginDate;
	}

	public static Date endOfDay(Date date) {
		if(Objects.isNull(date)) return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY,23);
		cal.set(Calendar.MINUTE,59);
		cal.set(Calendar.SECOND,59);
		Date endDate = cal.getTime();
		return endDate;
	}

	/**
	 * 将集合转换成指定bean格式
	 * @param bean
	 * @param list
	 * @param <E>
	 * @return
	 */
	public static  <E> List<GeneralBean> getCollect(GeneralBean bean, List<E> list) {
		return list.stream().map(item->{
			JsonResultParamMapAnnotation annotation = item.getClass().getAnnotation(JsonResultParamMapAnnotation.class);
			GeneralBean generalBean = bean;
			if (!Objects.isNull(annotation)) {
				generalBean = setGeneralBean(item, annotation,bean);
				// log.info(generalBean.toString());
			}
			return generalBean;
		}).collect(Collectors.toList());
	}

	/**
	 * 装配bean
	 * @param item
	 * @param annotation
	 * @param generalBean
	 * @param <E>
	 * @return
	 */
	public static  <E> GeneralBean setGeneralBean(E item, JsonResultParamMapAnnotation annotation, GeneralBean generalBean) {
		GeneralBean bean = generalBean;
		try {
			//将bean的属性类型存入数组
			Field[] generalBeanFields = generalBean.getClass().getDeclaredFields();
			Class[] classes = new Class[4];
			for (int i = 0; i <generalBeanFields.length ; i++) {
				classes[i] = generalBeanFields[i].getType();
			}
			//初始化装配到bean的参数
			Object param1 = null;
			Object param2 = null;
			Object param3 = null;
			Object param4 = null;
			Method[] methods = annotation.getClass().getDeclaredMethods();
			for (int i = 0; i <methods.length; i++) {
				if(!methods[i].getName().contains("param")){
					continue;
				}
				//得到注解中传入的值（注解中存放的是entity的某个属性）
				String annotationMethodValue = (String) methods[i].invoke(annotation);
				//初始化entity属性的值
				Object entityFieldValue = null;
				if (!annotationMethodValue.equals("")) {
					//通过注解的值得到这个entity的属性
					Field entityDeclaredField = item.getClass().getDeclaredField(annotationMethodValue);
					entityDeclaredField.setAccessible(true);
					//判断该字段是否为entity的外键关联字段
					JoinColumn JoinColumnAnnotation = entityDeclaredField.getAnnotation(JoinColumn.class);
					if (Objects.nonNull(JoinColumnAnnotation)){
						//通过这个属性的对象得到其类，并获取其id属性和id属性的值
						Class<?> fieldClass = entityDeclaredField.get(item).getClass();
						Field idField = fieldClass.getDeclaredField("id");
						idField.setAccessible(true);
						entityFieldValue = idField.get(entityDeclaredField.get(item));
					}else {
						entityFieldValue = entityDeclaredField.get(item);
					}
					//判断注解的方法名，将属性值赋给对应bean的参数
					switch (methods[i].getName()){
						case "param1" :
							param1 = entityFieldValue;
							break;
						case "param2" :
							param2 = entityFieldValue;
							break;
						case "param3" :
							param3 = entityFieldValue;
							break;
						case "param4" :
							param4 = entityFieldValue;
							break;
					}

				}

			}
			//初始化bean的有参构造器
			Constructor<? extends GeneralBean> constructor = generalBean.getClass().getConstructor(classes);
			constructor.setAccessible(true);
			//装配参数
			bean = constructor.newInstance(new Object[]{param1,param2,param3,param4});

		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return bean;
	}

	/**
	 * 将实体集合转换为map集合
	 * @param list
	 * @param <E>
	 * @return
	 */
	public static  <E> List<Map> entityTransfer(List<E> list){
		List<Map> mapList = list.stream().map(entity -> {
			Map<String, Object> map = JsonResultParamMapProHandler(entity);
			return map;
		}).collect(Collectors.toList());
		return mapList;
	}

	/**
	 * 将实体中的 JsonResultParamMapPro注解的内容：目标字段名=当前类字段名
	 * 转换为Map（Key：目标字段名 Value：当前类字段名的值）输出
	 * @param entity 包含JsonResultParamMapPro注解的对象
	 * @param <E>
	 * @return Map（Key：目标字段名 Value：当前类字段名的值）
	 */
	private static <E> Map<String, Object> JsonResultParamMapProHandler(E entity){
		JsonResultParamMapPro annotation = entity.getClass().getAnnotation(JsonResultParamMapPro.class);
		Map<String, Object> map = null;
		try {
			Method method = annotation.getClass().getDeclaredMethod("fieldMapping");
			map = new HashMap<>();
			String[] fieldMappings = (String[]) method.invoke(annotation);
			for (String fieldRelation : fieldMappings) {
				String[] fields = fieldRelation.split("=");
				String targeField = fields[0];
				String sourceField = fields[1];
				Field declaredField = entity.getClass().getDeclaredField(sourceField);
				declaredField.setAccessible(true);
				JoinColumn joinColumn = declaredField.getAnnotation(JoinColumn.class);
				Object value = null;
				if (Objects.nonNull(joinColumn)) {
					Class<?> fieldClass = declaredField.get(entity).getClass();
					Field idField = fieldClass.getDeclaredField("id");
					idField.setAccessible(true);
					value = idField.get(declaredField.get(entity));
				} else {
					value = declaredField.get(entity);
				}
				map.put(targeField, value);
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		return map;
	}
}
