using Microsoft.AspNet.Identity;
using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SqlClient;
using System.Linq;
using System.Web.Http;

namespace WebApplication1.Controllers
{
    //[AllowAnonymous]
    [Authorize]
    [RoutePrefix("api")]
    public class ValuesController : ApiController
    {

        homeinformer_azureEntities homeinformer = new homeinformer_azureEntities();

        /// <summary>
        /// All values in database
        /// </summary>
        /// <returns>List of all records from database in json format</returns>
        /// <seealso cref="getArchive()">All archived data</seealso>
        /// <seealso cref="getLastRecord()">Last added value</seealso>
        /// <seealso cref="insert(double, double, double, double, double, double)">Insert new value</seealso>
        /// <seealso cref="archiveData(int)">Archived data for the period(days)</seealso>
        [Route("getAll")]
        [HttpGet]
        [Authorize]
        [ActionName("Get all Records")]
        public IEnumerable<details> getHistory()
        {
            var CurrentUserName = User.Identity.GetUserName();
            return homeinformer.details.Where(entry => entry.userId == CurrentUserName).ToList();
        }

        /// <summary>
        /// All archived values in database
        /// </summary>
        /// <returns>List of all records from database in json format</returns>
        /// <seealso cref="getHistory()"/>
        [Route("getArchived")]
        [HttpGet]
        [ActionName("Get all Archived Records")]
        public IEnumerable<archive> getArchive()
        {
            var CurrentUserName = User.Identity.GetUserName();
            return homeinformer.archive.Where(entry => entry.userId == CurrentUserName).ToList();
        }



        /// <summary>
        /// Last added value from database
        /// </summary>
        /// <returns>Json object from database</returns>
        /// <seealso cref="getHistory()"/>
        /// <seealso cref="getArchive()"/>
        [Route("getLastRecord")]
        [HttpGet]
        public object getLastRecord()
        {
            var CurrentUserName = User.Identity.GetUserName();
            int lastInsertedRecord = homeinformer.details.Where(x => x.userId == CurrentUserName).Max(p => p.detailsId);
            details details = homeinformer.details.Find(lastInsertedRecord);
            if (details == null)
            {
                return NotFound();
            }
            return details;
        }


        /// <summary>
        /// Insert new record
        /// </summary>
        /// <returns>200 HTTP.OK if value was added or error message if no</returns>
        /// <seealso cref="getHistory"/>
        /// <param temperature1="T1" temperature2="T2" humidity="HMDT" luminosity="LMNST" pressure="PRSR" altitude="ALTTD"> Value insert parametres</param>
        [Route("insert")]
        [HttpGet]
        public Object insert(double T1, double T2, double HMDT, double LMNST, double PRSR, double ALTTD)
        {
            try
            {
                var CurrentUserName = User.Identity.GetUserName();
                var newRecord = new details()
                {
                    userId = CurrentUserName,
                    temperature1 = T1,
                    temperature2 = T2,
                    humidity = HMDT,
                    luminosity = LMNST,
                    pressure = PRSR,
                    altitude = ALTTD,
                    datetime = DateTime.Now.AddHours(3)
                };
                homeinformer.details.Add(newRecord);
                homeinformer.SaveChanges();
                return new { result = "200 HTTP.OK" };
            }
            catch (Exception e)
            {
                return new { result = e.Message };
            }
        }

        /// <summary>
        /// Get db size
        /// </summary>     
        /// <returns>Json object from database with one double argument</returns>
        [Route("getdbsize")]
        [HttpGet]
        public Object getDbSize()
        {
            try
            {
                var sqlConn = homeinformer.Database.Connection as SqlConnection;
                var cmd = new SqlCommand("Get_DB_Size")
                {
                    CommandType = System.Data.CommandType.StoredProcedure,
                    Connection = sqlConn as SqlConnection
                };
                var adp = new SqlDataAdapter(cmd);
                var dataset = new DataSet();
                sqlConn.Open();
                adp.Fill(dataset);
                sqlConn.Close();
                return new { dbsize = dataset.Tables[0].Rows[0][0] };
            }
            catch (Exception e)
            {
                return new { result = e.Message };
            }
        }


        /// <summary>
        /// Delete the data for a certain period
        /// </summary>
        /// <returns>200 HTTP.OK if values were deleted or error message if no</returns>
        /// <seealso cref="archiveData(int)"/>
        /// <seealso cref="getArchive"/>
        /// <seealso cref="getHistory"/>
        /// <param period="period">Period in days for deleting</param>
        [Route("delete")]
        [HttpGet]
        public Object delete(int period)
        {
            try
            {
                var CurrentUserName = User.Identity.GetUserName();
                DateTime maxDate = DateTime.Now.AddHours(3);
                DateTime minDate = maxDate.AddDays(-period);
                var results = homeinformer.details.Where(entry => entry.datetime >= minDate && entry.datetime <= maxDate && entry.userId == CurrentUserName).ToList();
                foreach (var item in results)
                {
                    homeinformer.details.Attach(item);
                    homeinformer.details.Remove(item);
                    homeinformer.SaveChanges();
                }
                return new { result = "200 HTTP.OK" };
            }
            catch (Exception e)
            {
                return new { result = e.Message };
            }
        }


        /// <summary>
        /// Archive the data for a certain period
        /// </summary>
        /// <returns>200 HTTP.OK if values were archived or error message if no</returns>
        /// <seealso cref="delete(int)"/>
        /// <seealso cref="getArchive"/>
        /// <seealso cref="getHistory"/>
        /// <param period="period">Period in days for archiving</param>
        [Route("archiveddata")]
        [HttpGet]
        public Object archiveData(int period)
        {
            try
            {
                var CurrentUserName = User.Identity.GetUserName();
                DateTime maxDate = DateTime.Now.AddHours(3);
                DateTime minDate = maxDate.AddDays(-period);
                List<details> result = homeinformer.details.Where(entry => entry.datetime >= minDate && entry.datetime <= maxDate && entry.userId == CurrentUserName).ToList();
                var newRecord = new archive()
                {
                    userId = CurrentUserName,
                    temp1A = result.Average(details => details.temperature1),
                    temp2A = result.Average(details => details.temperature2),
                    humiditiA = result.Average(details => details.humidity),
                    luminosityA = result.Average(details => details.luminosity),
                    pressureA = result.Average(details => details.pressure),
                    altitudeA = result.Average(details => details.altitude),
                    timeperiodA = minDate.ToString() + "--" + maxDate.ToString()
                };
                homeinformer.archive.Add(newRecord);
                homeinformer.SaveChanges();
                foreach (var item in result)
                {
                    homeinformer.details.Attach(item);
                    homeinformer.details.Remove(item);
                    homeinformer.SaveChanges();
                }
                return new { result = "200 HTTP.OK" };
            }
            catch (Exception e)
            {
                return new { result = e.Message };
            }
        }

        /// <summary>
        /// All setting strings
        /// </summary>
        /// <returns>List of all records from database in json format</returns>
        /// <seealso cref="getHistory"/>
        /// <seealso cref="getArchive"/>
        [Route("getsettings")]
        [HttpGet]
        [ActionName("Get settings strings")]
        public IEnumerable<settings> getSettings()
        {
            var CurrentUserName = User.Identity.GetUserName();
            return homeinformer.settings.Where(entry => entry.userId == CurrentUserName).ToList();
        }

        /// <summary>
        /// Update setting field
        /// </summary>
        /// <returns>200 HTTP.OK if setting values were updated or error message if no</returns>
        /// <seealso cref="getSettings"/>
        /// <seealso cref="getSetting(string)"/>
        /// <param description="description">Field which you want to update</param>
        /// <param vale="value">Value for field which you want update</param>
        [Route("updatesetting")]
        [HttpGet]
        public Object updateSetting(string description, string value)
        {
            try
            {
                var CurrentUserName = User.Identity.GetUserName();
                settings sttng = homeinformer.settings.Where(p => p.userId == CurrentUserName).First(i => i.describe == description);
                sttng.value = value;
                homeinformer.SaveChanges();
                return new { result = "200 HTTP.OK" };
            }
            catch (Exception e)
            {
                return new { result = e.Message };
            }
        }

        /// <summary>
        /// Get special setting value by name
        /// </summary>
        /// <returns>Description and value if exist</returns>
        /// <seealso cref="getSettings"/>
        /// <seealso cref="updateSetting(string, string)"/>
        /// <param description="description">Description field for which you want to get active value</param>
        [Route("getsetting")]
        [HttpGet]
        public Object getSetting(string description)
        {
            var CurrentUserName = User.Identity.GetUserName();
            var dataset = homeinformer.settings.Where(x => x.describe == description && x.userId == CurrentUserName).Select(x => new { x.value }).FirstOrDefault();
            if (dataset == null)
            {
                return NotFound();
            }
            return dataset;
        }


    }
}
