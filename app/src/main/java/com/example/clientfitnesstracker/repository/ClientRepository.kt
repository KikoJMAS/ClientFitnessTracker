package com.example.clientfitnesstracker.repository

import com.example.clientfitnesstracker.data.db.dao.ClientDao
import com.example.clientfitnesstracker.data.db.entities.Client
import kotlinx.coroutines.flow.Flow

class ClientRepository(private val clientDao: ClientDao) {

    fun getClients(): Flow<List<Client>> = clientDao.getAllClients()

    suspend fun addClient(client: Client) {
        clientDao.insertClient(client)
    }

    suspend fun deleteClient(client: Client) {
        clientDao.deleteClient(client)
    }
}
