package com.kingh.core.bundle;

import com.kingh.config.env.EnvProperties;
import com.kingh.core.anno.Action;
import com.kingh.core.api.Operate;
import com.kingh.core.bean.BundleBean;
import com.kingh.core.bean.OperateBean;
import com.kingh.core.scan.ScanFace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 组件门面，外部调用组件相关操作，均通过此类
 */
public class BundleManagerFace {

    private static final Logger logger = LoggerFactory.getLogger(BundleManagerFace.class);

    /**
     * 维护所有的操作
     */
    private static ConcurrentHashMap<String, OperateBean> operates = new ConcurrentHashMap<>();

    /**
     * 所有的组件
     */
    private static ConcurrentHashMap<String, BundleBean> bundles = new ConcurrentHashMap<>();

    /**
     * 组件放置的路径
     */
    private static final String ext_bundle_path = EnvProperties.getExtClassPath();

    /**
     * 构建bundle
     * <p>
     * 扫描指定路径下的所有jar，维护组件及组件中的操作
     */
    public static void buildBundle() {
        Set<File> bundleFiles = ScanFace.listDirFiles(ext_bundle_path, s -> s.endsWith(".jar"));
        bundleFiles.stream().forEach(f -> {

            String bundleName = f.getName();
            String bundleFilePath = f.getAbsolutePath();
            long lastModify = f.lastModified();

            BundleBean bundle = bundles.get(bundleName);
            if (bundle == null) {
                logger.info(bundleName + " 组件首次加载...");
                bundle = new BundleBean();
                bundle.setFullName(bundleName);
                bundle.setPath(bundleFilePath);
                bundle.setLastUpdateTime(lastModify);
                bundles.put(bundleName, bundle);
            } else {
                long updateTime = bundle.getLastUpdateTime();
                if (lastModify == updateTime) {
                    return;
                } else {
                    logger.info(bundleName + " 组件被修改了，正在重新加载... ");
                    bundle.setLastUpdateTime(lastModify);
                }
            }

            loadBundle(bundle);
            logger.info(bundleName + " 组件加载完成");
        });
    }

    public static void loadBundle(BundleBean bundle) {

        // 解析
        Set<String> jarFiles = ScanFace.listJarFile(bundle.getPath(), c -> !c.endsWith("/"));
        for (String jarFile : jarFiles) {
            if (jarFile.endsWith(".class")) {
                String className = jarFile.replaceAll("/", "\\.").replaceAll(".class", "");
                Class clazz = ScanFace.loadExtJarClass(bundle.getPath(), className);
                if (clazz.isAnnotationPresent(Action.class)) {
                    Action action = (Action) clazz.getAnnotation(Action.class);

                    OperateBean operateBean = new OperateBean();
                    operateBean.setBundle(bundle);
                    operateBean.setClassName(className);
                    operateBean.setId(action.id());
                    operateBean.setName(action.name());
                    operateBean.setDescription(action.desc());
                    operateBean.setNext(action.next());
                    operateBean.setClazz(clazz);

                    bundle.getOperates().add(operateBean);

                    operates.put(operateBean.getId(), operateBean);
                }
            }
        }
    }

    /**
     * 查询操作
     *
     * @param id
     * @return
     */
    public static OperateBean queryOperate(String id) {
        return operates.get(id);
    }

    /**
     * 查组件的操作
     *
     * @param bundleName
     * @return
     */
    public static Set<OperateBean> queryOperates(String bundleName) {
        BundleBean bundle = bundles.get(bundleName);
        if (bundle == null) {
            return Collections.emptySet();
        }
        return bundle.getOperates();
    }
}
