
package com.example.clientfitnesstracker.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.clientfitnesstracker.model.Client
import com.example.clientfitnesstracker.model.WorkoutEntry

class ClientViewModel : ViewModel() {

    // Internal mutable list of clients
    private val _clients = mutableStateListOf<Client>()

    // Public read-only access to the clients list
    val clients: List<Client> = _clients

    // Add a new client
    fun addClient(client: Client) {
        _clients.add(client)
    }

    // Delete an existing client
    fun deleteClient(client: Client) {
        _clients.remove(client)
    }

    // Update an existing client by ID
    fun updateClient(updatedClient: Client) {
        _clients.replaceAll { if (it.id == updatedClient.id) updatedClient else it }
    }

    // Get a client by ID
    fun getClientById(id: Int): Client? {
        return _clients.find { it.id == id }
    }

    // Add a workout entry to a specific client
    fun addWorkoutToClient(clientId: Int, entry: WorkoutEntry) {
        val index = _clients.indexOfFirst { it.id == clientId }
        if (index != -1) {
            val client = _clients[index]
            val updatedHistory = client.history + entry
            val updatedClient = client.copy(history = updatedHistory)
            _clients[index] = updatedClient
        }
    }
}
