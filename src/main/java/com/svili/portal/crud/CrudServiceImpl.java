package com.svili.portal.crud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.svili.portal.crud.common.PersistentUtil;
import com.svili.portal.crud.utils.GeneralMapperReflectUtil;
import com.svili.portal.dao.GeneralDao;

/**
 * 通用Crud 数据访问层接口实现类
 * 
 * @author svili
 * @date 2016年11月11日
 *
 */
@Service("crudService")
public class CrudServiceImpl implements CrudServiceInter {

	@Resource(name = "generalDao")
	private GeneralDao dao;

	@Override
	public <T> T selectByPrimaryKey(Class<T> clazz, Object primaryValue) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();

		String tableName = PersistentUtil.getTableName(clazz);
		String primaryKey = PersistentUtil.getPrimaryKey(clazz);
		List<String> queryColumn = GeneralMapperReflectUtil.getAllColumnNames(clazz);

		param.put("tableName", tableName);
		param.put("primaryKey", primaryKey);
		param.put("primaryValue", primaryValue);
		param.put("queryColumn", queryColumn);

		return GeneralMapperReflectUtil.parseToBean(dao.selectByPrimaryKey(param), clazz);
	}

	@Override
	public <T> int insertSelective(T t) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();

		Class<?> clazz = t.getClass();

		String tableName = PersistentUtil.getTableName(clazz);
		List<SQLColumn> SQLColumns = GeneralMapperReflectUtil.getSQLColumnsExceptNull(t);

		param.put("tableName", tableName);
		param.put("SQLColumns", SQLColumns);

		return dao.insert(param);
	}

	@Override
	public <T> int insert(T t) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();

		Class<?> clazz = t.getClass();

		String tableName = PersistentUtil.getTableName(clazz);
		List<SQLColumn> SQLColumns = GeneralMapperReflectUtil.getAllSQLColumns(t);

		param.put("tableName", tableName);
		param.put("SQLColumns", SQLColumns);
		return dao.insert(param);
	}

	@Override
	public <T> int insertBatch(List<T> list) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();

		String tableName = "";
		List<String> columnNames = new ArrayList<String>();

		List<List<SQLColumn>> dataList = new ArrayList<List<SQLColumn>>(list.size() + 1);

		for (T t : list) {
			if (tableName.equals("")) {
				Class<?> clazz = t.getClass();
				tableName = PersistentUtil.getTableName(clazz);
			}
			if (columnNames.size() == 0) {
				Class<?> clazz = t.getClass();
				columnNames = GeneralMapperReflectUtil.getAllColumnNames(clazz);
			}
			List<SQLColumn> SQLColumns = GeneralMapperReflectUtil.getAllSQLColumns(t);
			dataList.add(SQLColumns);
		}

		param.put("tableName", tableName);
		param.put("columnNames", columnNames);
		param.put("dataList", dataList);

		return dao.insertBatch(param);
	}

	@Override
	public <T> int deleteByPrimaryKey(Class<T> clazz, Object primaryValue) {
		Map<String, Object> param = new HashMap<String, Object>();

		String tableName = PersistentUtil.getTableName(clazz);
		String primaryKey = PersistentUtil.getPrimaryKey(clazz);

		param.put("tableName", tableName);
		param.put("primaryKey", primaryKey);
		param.put("primaryValue", primaryValue);

		return dao.deleteByPrimaryKey(param);
	}

	@Override
	public <T> int deleteByCondition(Class<T> clazz, String conditionExp, Map<String, Object> conditionParam) {
		Map<String, Object> param = new HashMap<String, Object>();

		String tableName = PersistentUtil.getTableName(clazz);

		param.put("tableName", tableName);
		param.put("conditionExp", conditionExp);
		param.put("conditionParam", conditionParam);

		return dao.deleteByCondition(param);
	}

	@Override
	public <T> int updateByPrimaryKey(T t) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();

		Class<?> clazz = t.getClass();

		String tableName = PersistentUtil.getTableName(clazz);
		String primaryKey = PersistentUtil.getPrimaryKey(clazz);

		List<SQLColumn> SQLColumns = GeneralMapperReflectUtil.getSQLColumns(t,false,true);

		Object primaryValue = GeneralMapperReflectUtil.getPrimaryValue(t);

		param.put("tableName", tableName);
		param.put("primaryKey", primaryKey);
		param.put("primaryValue", primaryValue);
		param.put("SQLColumns", SQLColumns);

		return dao.updateByPrimaryKey(param);
	}

	@Override
	public <T> int updateByPrimaryKeySelective(T t) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();

		Class<?> clazz = t.getClass();

		String tableName = PersistentUtil.getTableName(clazz);
		String primaryKey = PersistentUtil.getPrimaryKey(clazz);

		List<SQLColumn> SQLColumns = GeneralMapperReflectUtil.getSQLColumns(t,false,false);

		Object primaryValue = GeneralMapperReflectUtil.getPrimaryValue(t);

		param.put("tableName", tableName);
		param.put("primaryKey", primaryKey);
		param.put("primaryValue", primaryValue);
		param.put("SQLColumns", SQLColumns);

		return dao.updateByPrimaryKey(param);
	}

	@Override
	public <T> int updateByConditionSelective(Class<T> clazz, Map<String, Object> columnValueMapping,
			String conditionExp, Map<String, Object> conditionParam) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();

		String tableName = PersistentUtil.getTableName(clazz);

		param.put("tableName", tableName);
		param.put("columnValueMapping", columnValueMapping);
		param.put("conditionExp", conditionExp);
		param.put("conditionParam", conditionParam);

		return dao.updateByConditionSelective(param);
	}

	@Override
	public <T> List<T> selectAdvanced(Class<T> clazz, GeneralQueryParam queryParam) throws Exception {
		List<T> result = new ArrayList<T>();

		queryParam.setQueryColumn(GeneralMapperReflectUtil.getAllColumnNames(clazz));

		List<Map<String, Object>> list = selectAdvancedByColumn(clazz, queryParam);

		if (list != null && list.size() != 0) {
			for (Map<String, Object> mapping : list) {
				result.add(GeneralMapperReflectUtil.parseToBean(mapping, clazz));
			}
		}
		return result;
	}

	@Override
	public <T> List<Map<String, Object>> selectAdvancedByColumn(Class<T> clazz, GeneralQueryParam queryParam)
			throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();

		String tableName = PersistentUtil.getTableName(clazz);

		param.put("tableName", tableName);
		param.put("queryColumn", queryParam.getQueryColumn());
		param.put("conditionExp", queryParam.getConditionExp());
		param.put("conditionParam", queryParam.getConditionParam());
		param.put("orderExp", queryParam.getOrderExp());

		if (queryParam.getPageSize() != null && queryParam.getPageNo() != null) {
			Map<String, Integer> page = new HashMap<String, Integer>();
			page.put("pageSize", queryParam.getPageSize());
			page.put("startRowNo", (queryParam.getPageNo() - 1) * queryParam.getPageSize());
			param.put("page", page);
		}

		return dao.selectAdvanced(param);
	}

}
