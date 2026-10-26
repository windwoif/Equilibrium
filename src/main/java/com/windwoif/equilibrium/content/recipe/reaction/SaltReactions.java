package com.windwoif.equilibrium.content.recipe.reaction;

/**
 * 盐反应占位。
 * 溶解/熔融反应不再在此注册或映射——由 Reactor 按当前 contents 动态生成,
 * 参与物(离子/熔融盐)按 name 直接从 ChemicalRegistry 查找。
 */
public class SaltReactions {
    public static void init() {
    }
}
