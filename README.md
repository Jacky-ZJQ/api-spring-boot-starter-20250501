# 设计模式在牛马搬砖中的使用

## 开发顺序
1. 基础枚举类 （不依赖任何其他类）
   - ExcelImportTypeEnum ：定义全量/增量导入类型
   - ImportDataTypeEnum ：定义日志数据类型（汇总/失败/警告/正常）
2. 数据模型类 （依赖枚举类）
   - DataImportLog ：定义导入日志的数据结构
3. 自定义注解 （不依赖其他业务类）
   - StrategyModel ：用于标识策略类并指定策略名称
4. 核心接口 （依赖数据模型）
   - IExcelServiceFactory ：工厂接口，定义导入方法的入口
   - IExcelStrategyProcess ：策略接口，定义Excel导入流程的各个具体步骤
5. 策略支持类 （依赖注解和策略接口）
   - ExcelStrategySupport ：实现策略的自动注册和获取，是策略模式的核心实现
6. 抽象导入类 （依赖多个组件）
   - AbstractImportExcel ：使用模板方法模式，定义导入流程的模板，依赖ExcelStrategySupport和两个核心接口
7. 具体实现类 （依赖抽象类）
   - ExcelImpl ：实现AbstractImportExcel中的抽象方法，处理导入结果构建
8. 自动配置类 （依赖工厂接口和具体实现）
   - ExcelAutoConfig ：Spring Boot自动配置类，自动注册Excel服务工厂实例

## 项目中使用的设计模式：
- 抽象工厂模式 ：
  - 通过 IExcelServiceFactory 接口定义Excel导入服务工厂
- 模板方法模式 ：
  - 在 AbstractImportExcel 中定义导入流程的模板，具体实现延迟到子类
- 策略模式 ：
  - 通过 IExcelStrategyProcess 接口和 StrategyModel 注解 + ExcelStrategySupport类 实现支持不同的Excel导入策略

### 抽象工厂模式
IExcelServiceFactory.doImportExcel 定义好了Excel工厂的能力

### 模版方法模式
AbstractImportExcel.doImportExcel 定义好了导入Excel应该做什么的流程模版（抽象层面）
   - ExcelStrategySupport：获取 IExcelStrategyProcess 策略实例
   - ExcelImpl：继承AbstractImportExcel，并通过ExcelAutoConfig 在Spring启动时实例化

### 策略模式 
IExcelStrategyProcess 定义好了策略具体能做什么
   - IExcelStrategyProcess（定义好了策略具体能做什么） --> AbstractBaseImportExcel(默认的实现) --> StudentExcelStrategy（具体的执行延迟到子类）
   - StrategyModel注解在StudentExcelStrategy等用户自己实现的策略类中


### ExcelStrategySupport
为了更好地理解ExcelStrategySupport.onApplicationEvent方法的作用，我将整个类的工作流程整理如下：
1. Spring 容器启动
2. 创建实例 ：Spring 容器创建 ExcelStrategySupport 实例，调用 ExcelStrategySupport() 构造函数
3. 注入上下文 ：Spring 容器调用 setApplicationContext() 方法，将 ApplicationContext 注入到实例中
4. 容器初始化完成 ：当 Spring 容器初始化完成后，触发 ContextRefreshedEvent 事件
5. 加载策略 ：由于 ExcelStrategySupport 实现了 ApplicationListener<ContextRefreshedEvent> 接口，Spring 容器会调用 onApplicationEvent() 方法
6. 注册策略 ：在 onApplicationEvent() 方法中，使用之前保存的 applicationContext 获取所有带有 StrategyModel 注解的策略类，并将它们注册到 IMPORT_EXCEL_STRATEGY_MAP 中

#### 设计意图
这种设计是为了实现 策略的自动注册和获取 ，主要有以下优点：
- 不需要手动配置每个策略类，提高了代码的灵活性和可扩展性
- 利用 Spring 容器的生命周期管理，确保在容器完全初始化后再加载策略
- 统一管理所有策略类，方便后续根据策略名称获取对应的策略实现
  通过这种方式，项目可以轻松扩展新的 Excel 导入策略，只需要创建一个实现了 IExcelStrategyProcess 接口并使用 @StrategyModel 注解标识的类即可，Spring 容器会自动将其注册到策略映射中。

