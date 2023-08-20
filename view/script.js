const url = "http://localhost:8080/task/user/1";

function hideLoader() {
    document.getElementById('loading').style.display = "none";
}

function renderTable(tasks) {
    let table = `<thead>
                    <th scope="col">#</th>
                    <th scope="col">Description</th>
                    <th scope="col">username</th>
                    <th scope="col">User Id</th>
                </thead>`;

    for (let task of tasks) {
        table += `
            <tr>
                <td scope="row"> ${task.id}</td>
                <td scope="row"> ${task.user.description}</td>
                <td scope="row"> ${task.user.username}</td>
                <td scope="row"> ${task.user.id}</td>
            </tr>
        `;
    }

    document.getElementById('tasks').innerHTML = table;
    hideLoader();
}

async function getAPI(url) {
    const response = await fetch(url, { method: "GET" });

    if (response.ok) {
        const data = await response.json();
        console.log(data);
        renderTable(data);
    } else {
        console.error("Error fetching data");
    }
}

getAPI(url);
