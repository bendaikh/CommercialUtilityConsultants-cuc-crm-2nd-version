$(function () {
    $('#messages li').click(function () {
        $(this).fadeOut();
    });
    setTimeout(function () {
        $('#messages li.info').fadeOut();
    }, 3000);
});

function retrieveGas(div, id) {
    var url = '/admin/customer/annualGasConsumption';

    $("#" + div).load(url, {
        id: id
    });
}

function retrieveElectricity(div, id) {
    var url = '/admin/customer/annualElectricityConsumption';

    $("#" + div).load(url, {
        id: id
    });
}

$(function () {
    $("#altDueByDate").datepicker({
        changeMonth: true,
        changeYear: true,
        dateFormat: "dd/mm/yy",
        altFormat: "yy-mm-dd",
        altField: "#dueByDate",
        yearRange: "-4:+10",
        hideIfNoPrevNext: true
    });
});

$(function () {
    $("#altReplyDueByDate").datepicker({
        changeMonth: true,
        changeYear: true,
        dateFormat: "dd/mm/yy",
        altFormat: "yy-mm-dd",
        altField: "#replyDueByDate",
        yearRange: "-4:+10",
        hideIfNoPrevNext: true
    });
});

$(function () {
    $("#altDob").datepicker({
        changeMonth: true,
        changeYear: true,
        dateFormat: "dd/mm/yy",
        altFormat: "yy-mm-dd",
        altField: "#dob",
        yearRange: "-90:+4",
        hideIfNoPrevNext: true
    });
});

$(function () {
    $("#altStartDate").datepicker({
        changeMonth: true,
        changeYear: true,
        dateFormat: "dd/mm/yy",
        altFormat: "yy-mm-dd",
        altField: "#startDate",
        yearRange: "-4:+10",
        hideIfNoPrevNext: true
    });
});

$(function () {
    $("#altEndDate").datepicker({
        changeMonth: true,
        changeYear: true,
        dateFormat: "dd/mm/yy",
        altFormat: "yy-mm-dd",
        altField: "#endDate",
        yearRange: "-4:+10",
        hideIfNoPrevNext: true
    });
});

$(function () {
    $("#altValidFrom").datepicker({
        changeMonth: true,
        changeYear: true,
        dateFormat: "dd/mm/yy",
        altFormat: "yy-mm-dd",
        altField: "#validFrom",
        yearRange: "-4:+4",
        hideIfNoPrevNext: true
    });
});

$(function () {
    $("#altValidTo").datepicker({
        changeMonth: true,
        changeYear: true,
        dateFormat: "dd/mm/yy",
        altFormat: "yy-mm-dd",
        altField: "#validTo",
        yearRange: "-4:+10",
        hideIfNoPrevNext: true
    });
});

$(function () {
    $("#fromDate").datepicker({
        changeMonth: true,
        changeYear: true,
        dateFormat: "yy-mm-dd",
        yearRange: "-4:+4",
        hideIfNoPrevNext: true
    });
});

$(function () {
    $("#toDate").datepicker({
        changeMonth: true,
        changeYear: true,
        dateFormat: "yy-mm-dd",
        yearRange: "-4:+10",
        hideIfNoPrevNext: true
    });
});

$(function () {
    $("#altIncorporationDate").datepicker({
        changeMonth: true,
        changeYear: true,
        dateFormat: "dd/mm/yy",
        altFormat: "yy-mm-dd",
        altField: "#incorporationDate",
        yearRange: "-4:+10",
        hideIfNoPrevNext: true
    });
});


/*$(function() {
    $( "#callbackDate" ).datepicker({
      changeMonth: true,
      changeYear: true,
	  dateFormat: "yy-mm-dd",
	  yearRange: "-0:+4",
	  hideIfNoPrevNext: true
    });
});*/


/***DOCUMENT UPLOAD USING AJAX****/
//bind the on-change event
/*$(document).ready(function() {
  $("#upload-file-input").on("change", uploadFile);
});*/

/**
 * Upload the file sending it via Ajax at the Spring Boot server.
 */

/*function uploadFile() {
  $.ajax({
    url: "/uploadFile",
    type: "POST",
    data: new FormData($("#upload-file-form")[0]),
    enctype: 'multipart/form-data',
    processData: false,
    contentType: false,
    cache: false,
    success: function () {
      // Handle upload success
      // ...
    },
    error: function () {
      // Handle upload error
      // ...
    }
  });
} // function uploadFile
*/


function formatDate(date) {
    var d = new Date(date),
        year = d.getFullYear(),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate();
    console.log('Formatted year: ' + d.getFullYear());
    if (month.length < 2)
        month = '0' + month;
    if (day.length < 2)
        day = '0' + day;

    return [year, month, day].join('-');
}

$(document).ready(function () {

    $('#contractMonthlyDuration').on('change', function () {

        var startDate = moment($('#startDate').val());
        var monthsToAdd = $('#contractMonthlyDuration').val();

        if (startDate.isValid()) {

            var endDate = startDate.clone().add(monthsToAdd, 'month');

            if (monthsToAdd > 0) {
                endDate = moment(endDate).subtract(1, "days").format("YYYY-MM-DD");
            } else {
                endDate = moment(endDate).format("YYYY-MM-DD");
            }


            var altEndDate = moment(endDate).format("DD/MM/YYYY");

            $('#endDate').val(endDate);
            $('#altEndDate').val(altEndDate);
        }
    });

    $('#altStartDate').on('change', function () {

        var startDate = moment($('#startDate').val());
        var monthsToAdd = $('#contractMonthlyDuration').val();

        if (startDate.isValid()) {

            var endDate = startDate.clone().add(monthsToAdd, 'month');

            if (monthsToAdd > 0) {
                endDate = moment(endDate).subtract(1, "days").format("YYYY-MM-DD");
            } else {
                endDate = moment(endDate).format("YYYY-MM-DD");
            }

            var altEndDate = moment(endDate).format("DD/MM/YYYY");

            $('#endDate').val(endDate);
            $('#altEndDate').val(altEndDate);
        }
    });

    $("#form").validate({
        rules: {
            // simple rule, converted to {required:true}
            accountNumber: "required",
            // compound rule
            altStartDate: {
                required: function (element) {
                    return $("#logType").val() == "LIVE" || $("#logType").val() == "SOLD" || $("#logType").val() == "PROCESSING";
                }
            },
            altEndDate: {
                required: function (element) {
                    return $("#logType").val() == "LIVE" || $("#logType").val() == "SOLD" || $("#logType").val() == "PROCESSING";
                }
            },
            callbackDateTime: {
                required: function (element) {
                    return $("#logType").val() == "CALLBACK";
                }
            }
        },
        messages: {
            accountNumber: "<span class=\"text-danger\">This is a required field</span>",
            altStartDate: {
                required: "<span class=\"text-danger\">Start Date is required for LIVE, SOLD and PROCESSING contracts.</span>"
            },
            altEndDate: {
                required: "<span class=\"text-danger\">End Date is required for LIVE, SOLD and PROCESSING contracts.</span>"
            },
            callbackDateTime: {
                required: "<span class=\"text-danger\">Callback date and time is required for a CALLBACK.</span>"
            }
        }
    });
});

function showWelcomeOptions(id, supplyType) {
    $.get('/admin/tasks/welcomepack', {
        id: id,
        supplyType: supplyType
    }, function (data) {
        $('#welcomePack').html(data);
    });
}

function showProcessOptions(id, supplyType) {
    $.get('/admin/tasks/processing-pack', {
        id: id,
        supplyType: supplyType
    }, function (data) {
        $('#processPack').html(data);
    });
}

function showTerminationOptions(id, supplyType) {
    $.get('/admin/tasks/terminationtask', {
        id: id,
        supplyType: supplyType
    }, function (data) {
        $('#terminationTask').html(data);
    });
}

function showCustomerNote(id) {
    $.get('/admin/tasks/customernote', {
        id: id
    }, function (data) {
        $('#customernote').html(data);
    });
}

function showCustomerChildNote(id) {
    $.get('/admin/tasks/customerchildnote', {
        id: id
    }, function (data) {
        $('#customerchildnote').html(data);
    });
}

function showBrokerNote(id) {
    $.get('/admin/tasks/brokernote', {
        id: id
    }, function (data) {
        $('#brokernote').html(data);
    });
}

function showGasContractNotes(id) {
    $.get('/admin/customer/gasContractNotes', {
        id: id
    }, function (data) {
        $('#gasContractNotesDiv').html(data);
    });
}

function saveGasContractNote(id) {
    if ($('#gasNote').valid()) {
        $.post('/gasContractNote', $('#gasNote').serialize(), function (data) {
            showGasContractNotes(id);
            $('#gasNote').get(0).reset();
            $('#newNoteModal').modal('hide');
        });
    }
}

function loadLinkedAccounts(id){
    $.get('/admin/customer/linked-accounts/' + id, function (data) {
            $('#linked-accounts-div').html(data);
        });
}


function createLinkedAccounts(id) {
    $.get('/admin/customer/create-linked-accounts/' + id, function (data) {
        $('#linked-accounts-div').html(data);
    });
}

function searchCustomerReference(id, searchParam) {
    $.get('/admin/customer/create-linked-accounts/' + id, {
       searchParam: searchParam
   }, function (data) {
        $('#linked-accounts-div').html(data);
    });
}

function deleteLinkedAccount(id, linkedAccountId) {
    $.get('/admin/customer/delete-linked-account/' + id, {
       linkedAccountId: linkedAccountId
   }, function (data) {
       location.reload();
    });
}

function saveLinkedAccount(id, linkedAccountId) {
    $.post('/saveLinkedAccount', {
       id: id,
       linkedAccountId: linkedAccountId
   }, function (data) {
        location.reload();
    });
}

function showElecContractNotes(id) {
    $.get('/admin/customer/elecContractNotes', {
        id: id
    }, function (data) {
        $('#elecContractNotesDiv').html(data);
    });
}

function showUtilityContractNotes(id) {
    $.get('/admin/customer/utility-contract-notes', {
        id: id
    }, function (data) {
        $('#utility-contract-notes-div').html(data);
    });
}

function saveUtilityContractNote(id) {
    if ($('#utilityNote').valid()) {
        $.post('/utilityContractNote', $('#utilityNote').serialize(), function (data) {
            showUtilityContractNotes(id);
            $('#utilityNote').get(0).reset();
            $('#newNoteModal').modal('hide');
        });
    }
}

function saveElecContractNote(id) {
    if ($('#elecNote').valid()) {
        $.post('/elecContractNote', $('#elecNote').serialize(), function (data) {
            showElecContractNotes(id);
            $('#elecNote').get(0).reset();
            $('#newNoteModal').modal('hide');
        });
    }
}

function showTpsContactsList(id) {
    $.get('/admin/customer/contactslist/' + id, function (data) {
        $('#contactDetails').html(data);
    });
}

function sendNotificationEmail(url, emailAddress) {
    if (emailAddress == '') {
        alert('Please select an email address');
    } else {
        var passedUrl = url + '?emailAddress=' + emailAddress;
        window.location.href = passedUrl;
    }
}

function transferBrokerContracts(previousBroker, newBroker, supplyType, logType) {
    $('#'+supplyType+'_'+logType+'_button').addClass('disabled');
    $('#'+supplyType+'_'+logType+'_broker').attr('disabled', true);
    $('#'+supplyType+'_'+logType+'_update_panel').html('Updating...');

    $.post('/admin/broker/broker-transfer/transfer-broker-contracts', {
       previousBroker,
       newBroker,
       supplyType,
       logType
    }, function (data) {
        $('#transfer-modal').modal('hide');
        $('#'+supplyType+'_'+logType+'_update_panel').html(data);
    });
}

function transferUserNotes(previousUser, newUser) {
    $('#transfer_button').addClass('disabled');
    $('#update_panel').html('Updating...');
    $('#modal_button').addClass('disabled');
    $('#notes_new_user').attr('disabled', true);

    $.post('/admin/users/transfer-portal/transfer-user-notes', {
       previousUser,
       newUser,
    }, function (data) {
        $('#transfer-modal').modal('hide');
        $('#update_panel').html(data);
    });
}

function selectCallbackTime(time) {
    $('#callbackDateTime').val(time);
    $('#callback-diary').modal('hide');
}

function loadCustomerSearch(panel, customerSite, q) {
    $.get('/admin/customer/customer-search', {
    customerSite,
    q
    }, function (data) {
        $('#' + panel).html(data);
    });
}

function createSubFolder(parentId, subfolderName) {
    $.post('/admin/system-setting/document-settings/save-subfolder', {
       parentId,
       subfolderName,
    }, function (data) {
        location.reload();
    });
}
