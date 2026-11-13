package com.example.pw_04.data.model

/**
 * Власний виняток, який позначає збій під час отримання показань навантаження.
 *
 * @param message Повідомлення про помилку українською мовою.
 */
class DataFailureException(message: String) : Exception(message)
