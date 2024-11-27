package ru.topbun.features.tours

import ru.topbun.models.config.Config

object TourSchedulerManager {

    private val schedulers = mutableMapOf<String?, TourScheduler>()

    fun startSchedulers() {
        val configs = getConfigsFromResource()

        configs.forEach { config ->
            val scheduler = TourScheduler(config)
            scheduler.start()
            schedulers[config.city?.name] = scheduler
        }
    }

    fun updateConfig(newConfig: Config) {
        val name = newConfig.city?.name

        schedulers[name]?.cancel()

        val scheduler = TourScheduler(newConfig)
        scheduler.start()
        schedulers[name] = scheduler
    }

    private fun getConfigsFromResource(): List<Config> {
        return Config.getConfigFromResource()
    }
}