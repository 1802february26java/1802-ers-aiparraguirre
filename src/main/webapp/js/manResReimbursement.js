window.onload = () =>{

    document.getElementById('username').innerHTML=sessionStorage.getItem('employeeUsername');
   
    getResReimbursement();

}

function getResReimbursement(){
      
    let xhr = new XMLHttpRequest();

    xhr.onreadystatechange = () => {
          if(xhr.readyState === XMLHttpRequest.DONE && xhr.status ===200){
            
            let data = JSON.parse(xhr.responseText);
            console.log(data);
            
            showResReimbursement(data);
          }
      };
    console.log(xhr.open("GET",`multipleRequests.do?fetch=finalized`));
 
    xhr.send();
}

function showResReimbursement(data) {
     
    if(data.message){
        document.getElementById("listMessage").innerHTML = '<span class="label label-danger label-center">Something went wrong.</span>';
    }else{
    
        let reimbursementList = document.getElementById("resReimbursement");
             reimbursementList.innerHTML="";
        
        data.forEach((reimbursement)=>{
    
        let tr = document.createElement('tr');   

        let td1 = document.createElement('td');
        let td2 = document.createElement('td');
        let td3 = document.createElement('td');
        let td4 = document.createElement('td');
        let td5 = document.createElement('td');
        let td6 = document.createElement('td');
        let td7 = document.createElement('td');
        let td8 = document.createElement('td');
        let td9 = document.createElement('td');
          

        let txt1 = document.createTextNode(`${reimbursement.id}`);
        let txt2 = document.createTextNode(`${reimbursement.requested.year}-${reimbursement.requested.monthValue}-${reimbursement.requested.dayOfMonth}, ${reimbursement.requested.hour}:${reimbursement.requested.minute}:${reimbursement.requested.second}`);
        let txt3 = document.createTextNode(`${reimbursement.resolved.year}-${reimbursement.resolved.monthValue}-${reimbursement.resolved.dayOfMonth}, ${reimbursement.resolved.hour}:${reimbursement.resolved.minute}:${reimbursement.resolved.second}`); 
        console.log(reimbursement.requested);
        console.log(reimbursement.resolved);
        let txt4 = document.createTextNode(`${reimbursement.amount}`);
        let txt5 = document.createTextNode(`${reimbursement.description}`);
        let txt6 = document.createTextNode(`${reimbursement.requester.firstName} ${reimbursement.requester.lastName}`);
        
        let txt8 = document.createTextNode(`${reimbursement.type.type}`);
        let txt9 = document.createTextNode(`${reimbursement.status.status}`);
        
            td1.appendChild(txt1);
            td2.appendChild(txt2);
            td3.appendChild(txt3);
            td4.appendChild(txt4);
            td5.appendChild(txt5);
            td6.appendChild(txt6);
       //     td7.appendChild(txt7);
            td8.appendChild(txt8);
            td9.appendChild(txt9);
            
            tr.appendChild(td1);
            tr.appendChild(td2);
            tr.appendChild(td3);
            tr.appendChild(td4);
            tr.appendChild(td5);
            tr.appendChild(td6);
         //   tr.appendChild(td7);
            tr.appendChild(td8);
            tr.appendChild(td9);

            reimbursementList.appendChild(tr);
        });

    }
}