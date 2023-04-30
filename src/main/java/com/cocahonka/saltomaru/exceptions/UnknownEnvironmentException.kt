package com.cocahonka.saltomaru.exceptions

import org.bukkit.World.Environment

/**
 * Класс исключения для неизвестных мировых сред.
 *
 * @param environment мировая среда, которая вызвала исключение
 */
class UnknownEnvironmentException(environment: Environment) :
    IllegalStateException("Unknown world environment: $environment")