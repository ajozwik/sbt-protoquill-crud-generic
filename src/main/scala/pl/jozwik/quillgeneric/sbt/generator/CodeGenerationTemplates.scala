package pl.jozwik.quillgeneric.sbt.generator

object CodeGenerationTemplates extends CodeGenerationTemplates

trait CodeGenerationTemplates {
  val AliasGenericDeclaration        = "__ALIAS_GENERIC_DECLARATION__"
  val BeanClassImport                = "__BEAN_CLASS_IMPORT__"
  val BeanIdClassImport              = "__ID_CLASS_IMPORT__"
  val BeanIdTemplate                 = "__ID__"
  val BeanTemplate                   = "__BEAN__"
  val CreateOrUpdate                 = "__CREATE_OR_UPDATE__"
  val ContextAlias                   = "__CONTEXT_ALIAS__"
  val ContextTransactionEnd          = "__CONTEXT_TRANSACTION_END__"
  val ContextTransactionStart        = "__CONTEXT_TRANSACTION_START__"
  val CustomImports                  = "__CUSTOM_IMPORTS__"
  val DialectTemplate                = "__DIALECT__"
  val FindByKey                      = "__FIND_BY_KEY__"
  val Monad                          = "__MONAD__"
  val NamingTemplate                 = "__NAMING__"
  val PackageTemplate                = "__PACKAGE__"
  val RepositoryClassTemplate        = "__REPOSITORY_NAME__"
  val RepositoryDomainTraitImport    = "__REPOSITORY_DOMAIN_TRAIT_IMPORT__"
  val RepositoryImport               = "__REPOSITORY_IMPORT__"
  val RepositoryTraitImport          = "__REPOSITORY_TRAIT_IMPORT__"
  val RepositoryTraitSimpleClassName = "__REPOSITORY_TRAIT_SIMPLE_NAME__"
  val SqlIdiomImport                 = "__SQL_IDIOM_IMPORT__"
  val TryEnd                         = "__TRY_END__"
  val TryStart                       = "__TRY_START__"
  val Update                         = "__UP__"
}
